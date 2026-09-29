package com.codingfactory.backend.service.impl;

import com.codingfactory.backend.dto.CandidatureDto;
import com.codingfactory.backend.dto.CandidatureSubmitDto;
import com.codingfactory.backend.entity.Candidature;
import com.codingfactory.backend.entity.SujetPfe;
import com.codingfactory.backend.entity.Utilisateur;
import com.codingfactory.backend.enums.CandidatureStatus;
import com.codingfactory.backend.enums.Role;
import com.codingfactory.backend.exception.ResourceNotFoundException;
import com.codingfactory.backend.repository.CandidatureRepository;
import com.codingfactory.backend.repository.SujetPfeRepository;
import com.codingfactory.backend.repository.UtilisateurRepository;
import com.codingfactory.backend.service.CandidatureService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CandidatureServiceImpl implements CandidatureService {

    private final CandidatureRepository candidatureRepository;
    private final SujetPfeRepository sujetPfeRepository;
    private final UtilisateurRepository utilisateurRepository;

    public CandidatureServiceImpl(CandidatureRepository candidatureRepository,
                                 SujetPfeRepository sujetPfeRepository,
                                 UtilisateurRepository utilisateurRepository) {
        this.candidatureRepository = candidatureRepository;
        this.sujetPfeRepository = sujetPfeRepository;
        this.utilisateurRepository = utilisateurRepository;
    }

    @Override
    public CandidatureDto createCandidature(CandidatureDto dto) {
        SujetPfe sujet = sujetPfeRepository.findById(dto.getSujetPfeId())
                .orElseGet(() -> sujetPfeRepository.findAll().stream().findFirst()
                        .orElseGet(() -> {
                            SujetPfe s = new SujetPfe();
                            s.setTitre("Sujet PFE Général");
                            s.setDescription("Description du sujet PFE");
                            s.setDomaine("Informatique");
                            s.setTechnologie("Spring Boot / Angular");
                            s.setEntreprise("CodingFactory");
                            s.setActif(true);
                            return sujetPfeRepository.save(s);
                        }));

        Utilisateur candidat = utilisateurRepository.findById(dto.getCandidatId())
                .orElseThrow(() -> new ResourceNotFoundException("Utilisateur introuvable avec l'id : " + dto.getCandidatId()));

        Candidature candidature = new Candidature();
        candidature.setSujetPfe(sujet);
        candidature.setCandidat(candidat);
        candidature.setMessageMotivation(dto.getMessageMotivation());
        candidature.setStatut(CandidatureStatus.EN_ATTENTE);

        return mapToDto(candidatureRepository.save(candidature));
    }

    @Override
    public CandidatureDto submitCandidature(CandidatureSubmitDto dto) {
        Long targetSujetId = dto.getSujetPfeId();
        SujetPfe sujet = null;

        if (targetSujetId != null && targetSujetId > 0) {
            sujet = sujetPfeRepository.findById(targetSujetId).orElse(null);
        }

        if (sujet == null) {
            sujet = sujetPfeRepository.findAll().stream().findFirst().orElseGet(() -> {
                SujetPfe s = new SujetPfe();
                s.setTitre("Sujet PFE Innovation & IA 2026");
                s.setDescription("Projet de fin d'études axé sur le développement et la cybersécurité.");
                s.setDomaine("Informatique");
                s.setTechnologie("Spring Boot 3, Angular 18");
                s.setEntreprise("CodingFactory Labs");
                s.setActif(true);
                return sujetPfeRepository.save(s);
            });
        }

        String email = (dto.getEmail() != null && !dto.getEmail().trim().isEmpty())
                ? dto.getEmail().trim().toLowerCase()
                : "candidat." + System.currentTimeMillis() + "@codingfactory.tn";

        String nom = (dto.getNom() != null && !dto.getNom().trim().isEmpty()) ? dto.getNom().trim() : "Candidat";
        String prenom = (dto.getPrenom() != null && !dto.getPrenom().trim().isEmpty()) ? dto.getPrenom().trim() : "Etudiant";

        Utilisateur candidat = utilisateurRepository.findByEmail(email)
                .orElseGet(() -> {
                    Utilisateur u = new Utilisateur();
                    u.setNom(nom);
                    u.setPrenom(prenom);
                    u.setEmail(email);
                    u.setPassword("candidat");
                    u.setRole(Role.CANDIDAT);
                    return utilisateurRepository.save(u);
                });

        Candidature candidature = new Candidature();
        candidature.setSujetPfe(sujet);
        candidature.setCandidat(candidat);
        candidature.setMessageMotivation(dto.getMessageMotivation() != null ? dto.getMessageMotivation() : "Candidature PFE");
        candidature.setStatut(CandidatureStatus.EN_ATTENTE);

        return mapToDto(candidatureRepository.save(candidature));
    }

    @Override
    public CandidatureDto updateStatus(Long id, String status) {
        Candidature candidature = candidatureRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Candidature introuvable avec l'id : " + id));

        candidature.setStatut(CandidatureStatus.valueOf(status.toUpperCase()));
        return mapToDto(candidatureRepository.save(candidature));
    }

    @Override
    public List<CandidatureDto> getAllCandidatures() {
        return candidatureRepository.findAll().stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<CandidatureDto> getCandidaturesByCandidat(Long candidatId) {
        Utilisateur candidat = utilisateurRepository.findById(candidatId)
                .orElseThrow(() -> new ResourceNotFoundException("Utilisateur introuvable avec l'id : " + candidatId));

        return candidatureRepository.findByCandidat(candidat).stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    public CandidatureDto getById(Long id) {
        Candidature candidature = candidatureRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Candidature introuvable avec l'id : " + id));
        return mapToDto(candidature);
    }

    private CandidatureDto mapToDto(Candidature candidature) {
        return new CandidatureDto(
                candidature.getId(),
                candidature.getSujetPfe().getId(),
                candidature.getCandidat().getId(),
                candidature.getMessageMotivation(),
                candidature.getStatut()
        );
    }
}
