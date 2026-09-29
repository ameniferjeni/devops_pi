package com.codingfactory.pfe.controller;

import com.codingfactory.pfe.dto.PfeMatchResultDto;
import com.codingfactory.pfe.dto.PfeMatchingRequestDto;
import com.codingfactory.pfe.dto.PfeMatchingResponseDto;
import com.codingfactory.pfe.entity.SujetPfe;
import com.codingfactory.pfe.repository.SujetPfeRepository;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/pfe")
@CrossOrigin(origins = "*")
public class SujetPfeController {

    private final SujetPfeRepository repo;

    public SujetPfeController(SujetPfeRepository repo) { this.repo = repo; }

    @GetMapping("/sujets")
    public List<SujetPfe> list() { return repo.findAll(); }

    @GetMapping("/sujets/actifs")
    public List<SujetPfe> listActifs() { return repo.findByActifTrue(); }

    @PostMapping("/sujets")
    public ResponseEntity<SujetPfe> create(@Valid @RequestBody SujetPfe s) {
        return ResponseEntity.status(HttpStatus.CREATED).body(repo.save(s));
    }

    @PutMapping("/sujets/{id}")
    public ResponseEntity<SujetPfe> update(@PathVariable Long id, @Valid @RequestBody SujetPfe dto) {
        return repo.findById(id).map(sujet -> {
            sujet.setTitre(dto.getTitre());
            sujet.setDescription(dto.getDescription());
            sujet.setDomaine(dto.getDomaine());
            sujet.setTechnologie(dto.getTechnologie());
            sujet.setEntreprise(dto.getEntreprise());
            sujet.setActif(dto.isActif());
            return ResponseEntity.ok(repo.save(sujet));
        }).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/sujets/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        if (!repo.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        repo.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/sujets/match")
    public ResponseEntity<PfeMatchingResponseDto> calculateMatching(@RequestBody PfeMatchingRequestDto request) {
        List<SujetPfe> sujetsActifs = repo.findByActifTrue();
        if (sujetsActifs.isEmpty()) {
            sujetsActifs = repo.findAll();
        }

        List<String> userSkills = request.getCompetences() == null ? List.of() :
                request.getCompetences().stream()
                        .map(s -> s.trim().toLowerCase(Locale.ROOT))
                        .filter(s -> !s.isEmpty())
                        .collect(Collectors.toList());

        String prefDomaine = request.getDomainePrefere() == null ? "" : request.getDomainePrefere().toLowerCase(Locale.ROOT).trim();

        List<PfeMatchResultDto> results = new ArrayList<>();

        for (SujetPfe sujet : sujetsActifs) {
            int score = 40; // Base score
            List<String> matchees = new ArrayList<>();
            List<String> manquantes = new ArrayList<>();

            String sujetTech = sujet.getTechnologie() == null ? "" : sujet.getTechnologie().toLowerCase(Locale.ROOT);
            String sujetDomaine = sujet.getDomaine() == null ? "" : sujet.getDomaine().toLowerCase(Locale.ROOT);
            String sujetText = (sujet.getTitre() + " " + sujet.getDescription()).toLowerCase(Locale.ROOT);

            // Skill matching
            for (String skill : userSkills) {
                if (sujetTech.contains(skill) || sujetText.contains(skill)) {
                    score += 15;
                    matchees.add(skill);
                }
            }

            // Domain matching
            if (!prefDomaine.isEmpty() && (sujetDomaine.contains(prefDomaine) || prefDomaine.contains(sujetDomaine))) {
                score += 20;
            }

            // Cap score at 98%
            score = Math.min(score, 98);

            String status = "MOYEN";
            String reco = "Compatibilité modérée. Formation complémentaire recommandée.";
            if (score >= 80) {
                status = "EXCELLENT";
                reco = "Forte recommandation ! Votre profil correspond parfaitement à ce sujet PFE.";
            } else if (score >= 60) {
                status = "BON";
                reco = "Bonne adéquation technique. Candidature vivement conseillée.";
            }

            results.add(PfeMatchResultDto.builder()
                    .sujetId(sujet.getId())
                    .titre(sujet.getTitre())
                    .domaine(sujet.getDomaine())
                    .technologie(sujet.getTechnologie())
                    .entreprise(sujet.getEntreprise())
                    .scoreMatch(score)
                    .statutMatch(status)
                    .competencesMatchees(matchees)
                    .competencesManquantes(manquantes)
                    .recommandation(reco)
                    .build());
        }

        // Sort descending by scoreMatch
        results.sort(Comparator.comparingInt(PfeMatchResultDto::getScoreMatch).reversed());

        int topScore = results.isEmpty() ? 0 : results.get(0).getScoreMatch();

        return ResponseEntity.ok(PfeMatchingResponseDto.builder()
                .candidatNom(request.getCandidatNom() != null ? request.getCandidatNom() : "Candidat")
                .meilleurScore(topScore)
                .totalSujetsAnalyses(results.size())
                .resultats(results)
                .build());
    }
}
