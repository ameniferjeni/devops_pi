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
                .orElseThrow(() -> new ResourceNotFoundException("Sujet PFE introuvable avec l'id : " + dto.getSujetPfeId()));

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
        SujetPfe sujet = sujetPfeRepository.findById(dto.getSujetPfeId())
                .orElseThrow(() -> new ResourceNotFoundException("Sujet PFE introuvable avec l'id : " + dto.getSujetPfeId()));

        if (!sujet.isActif()) {
            throw new IllegalArgumentException("Ce sujet PFE n'accepte plus de candidatures.");
        }

        Utilisateur candidat = utilisateurRepository.findByEmail(dto.getEmail().trim().toLowerCase())
                .orElseGet(() -> {
                    Utilisateur u = new Utilisateur();
                    u.setNom(dto.getNom().trim());
                    u.setPrenom(dto.getPrenom().trim());
                    u.setEmail(dto.getEmail().trim().toLowerCase());
                    u.setPassword("candidat");
                    u.setRole(Role.CANDIDAT);
                    return utilisateurRepository.save(u);
                });

        Candidature candidature = new Candidature();
        candidature.setSujetPfe(sujet);
        candidature.setCandidat(candidat);
        candidature.setMessageMotivation(dto.getMessageMotivation());
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
