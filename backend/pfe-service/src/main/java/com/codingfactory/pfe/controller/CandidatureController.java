package com.codingfactory.pfe.controller;

import com.codingfactory.pfe.client.MlServiceClient;
import com.codingfactory.pfe.dto.MlPredictRequest;
import com.codingfactory.pfe.dto.MlPredictResponse;
import com.codingfactory.pfe.entity.Candidature;
import com.codingfactory.pfe.entity.SujetPfe;
import com.codingfactory.pfe.entity.Utilisateur;
import com.codingfactory.pfe.enums.CandidatureStatus;
import com.codingfactory.pfe.enums.Role;
import com.codingfactory.pfe.repository.CandidatureRepository;
import com.codingfactory.pfe.repository.SujetPfeRepository;
import com.codingfactory.pfe.repository.UtilisateurRepository;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/pfe")
@CrossOrigin(origins = "*")
public class CandidatureController {

    private static final Logger log = LoggerFactory.getLogger(CandidatureController.class);

    private final CandidatureRepository candidatureRepository;
    private final SujetPfeRepository sujetPfeRepository;
    private final UtilisateurRepository utilisateurRepository;
    private final MlServiceClient mlServiceClient;

    public CandidatureController(CandidatureRepository candidatureRepository,
                                 SujetPfeRepository sujetPfeRepository,
                                 UtilisateurRepository utilisateurRepository,
                                 MlServiceClient mlServiceClient) {
        this.candidatureRepository = candidatureRepository;
        this.sujetPfeRepository = sujetPfeRepository;
        this.utilisateurRepository = utilisateurRepository;
        this.mlServiceClient = mlServiceClient;
    }

    @GetMapping("/candidatures")
    public ResponseEntity<List<Candidature>> getAllCandidatures() {
        return ResponseEntity.ok(candidatureRepository.findAll());
    }

    @GetMapping("/candidatures/{id}")
    public ResponseEntity<Candidature> getById(@PathVariable Long id) {
        return candidatureRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/candidatures/candidat/{candidatId}")
    public ResponseEntity<List<Candidature>> getByCandidat(@PathVariable Long candidatId) {
        return ResponseEntity.ok(candidatureRepository.findByCandidatId(candidatId));
    }

    @PostMapping("/candidatures")
    public ResponseEntity<Candidature> createCandidature(@Valid @RequestBody Candidature c) {
        return ResponseEntity.status(HttpStatus.CREATED).body(candidatureRepository.save(c));
    }

    @PostMapping("/candidatures/submit")
    public ResponseEntity<?> submitCandidature(@RequestBody Map<String, Object> body) {
        Long sujetId = 0L;
        if (body.get("sujetPfeId") != null) {
            try {
                sujetId = Long.valueOf(body.get("sujetPfeId").toString());
            } catch (Exception ignored) {}
        }
        
        String nom = body.getOrDefault("nom", "Candidat").toString();
        String prenom = body.getOrDefault("prenom", "Anonyme").toString();
        String email = body.getOrDefault("email", "candidat@codingfactory.tn").toString();
        String messageMotivation = body.getOrDefault("messageMotivation", "Candidature soumise").toString();

        Long finalSujetId = sujetId;
        SujetPfe sujet = sujetPfeRepository.findById(finalSujetId)
                .orElseGet(() -> sujetPfeRepository.findAll().stream().findFirst()
                        .orElseGet(() -> {
                            SujetPfe s = new SujetPfe();
                            s.setTitre("Sujet PFE Innovation 2026");
                            s.setDescription("Projet de fin d'études axé sur le développement et la cybersécurité.");
                            s.setDomaine("Informatique");
                            s.setTechnologie("Spring Boot 3 / Angular 18");
                            s.setEntreprise("CodingFactory Labs");
                            s.setActif(true);
                            return sujetPfeRepository.save(s);
                        }));

        Utilisateur candidat = utilisateurRepository.findByEmail(email).orElseGet(() -> {
            Utilisateur u = new Utilisateur();
            u.setNom(nom);
            u.setPrenom(prenom);
            u.setEmail(email);
            u.setPassword("password123");
            u.setRole(Role.CANDIDAT);
            return utilisateurRepository.save(u);
        });

        Candidature c = new Candidature();
        c.setSujetPfe(sujet);
        c.setCandidat(candidat);
        c.setMessageMotivation(messageMotivation);
        c.setDateSoumission(LocalDateTime.now());
        c.setStatut(CandidatureStatus.EN_ATTENTE);

        Candidature saved = candidatureRepository.save(c);

        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @PatchMapping("/candidatures/{id}/status")
    public ResponseEntity<Candidature> updateStatus(@PathVariable Long id, @RequestParam String status) {
        return candidatureRepository.findById(id).map(c -> {
            String normalizedStatus = status.toUpperCase();
            if ("ACCEPTE".equals(normalizedStatus)) {
                normalizedStatus = "ACCEPTEE";
            } else if ("REFUSE".equals(normalizedStatus)) {
                normalizedStatus = "REFUSEE";
            }
            CandidatureStatus nextStatus = CandidatureStatus.valueOf(normalizedStatus);
            c.setStatut(nextStatus);

            if (nextStatus == CandidatureStatus.EN_ATTENTE) {
                c.setProbabiliteAcceptation(null);
                c.setScorePourcentage(null);
                c.setDecisionSuggeree(null);
                c.setExplicationMl(null);
                return ResponseEntity.ok(candidatureRepository.save(c));
            }

            Candidature saved = candidatureRepository.save(c);
            applyMlPrediction(saved);
            return ResponseEntity.ok(candidatureRepository.save(saved));
        }).orElse(ResponseEntity.notFound().build());
    }

    private void applyMlPrediction(Candidature candidature) {
        try {
            SujetPfe sujet = candidature.getSujetPfe();
            Utilisateur candidat = candidature.getCandidat();
            MlPredictRequest request = new MlPredictRequest(
                    candidature.getId(),
                    candidature.getMessageMotivation(),
                    sujet.getDomaine(),
                    sujet.getTechnologie(),
                    sujet.getEntreprise(),
                    sujet.isActif(),
                    candidatureRepository.countByCandidatId(candidat.getId()),
                    candidatureRepository.countBySujetPfeId(sujet.getId())
            );
            MlPredictResponse prediction = mlServiceClient.predict(request);
            candidature.setProbabiliteAcceptation(prediction.getProbabiliteAcceptation());
            candidature.setScorePourcentage(prediction.getScorePourcentage());
            candidature.setDecisionSuggeree(prediction.getDecisionSuggeree());
            candidature.setExplicationMl(prediction.getExplication());
        } catch (Exception exception) {
            log.warn("Impossible de calculer le score ML pour la candidature {}", candidature.getId(), exception);
        }
    }
}
