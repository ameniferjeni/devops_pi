package com.codingfactory.backend.service.impl;

import com.codingfactory.backend.dto.*;
import com.codingfactory.backend.entity.SujetPfe;
import com.codingfactory.backend.exception.ResourceNotFoundException;
import com.codingfactory.backend.repository.SujetPfeRepository;
import com.codingfactory.backend.service.SujetPfeService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

@Service
public class SujetPfeServiceImpl implements SujetPfeService {

    private final SujetPfeRepository sujetPfeRepository;

    public SujetPfeServiceImpl(SujetPfeRepository sujetPfeRepository) {
        this.sujetPfeRepository = sujetPfeRepository;
    }

    @Override
    public SujetPfeDto createSujet(SujetPfeDto dto) {
        SujetPfe sujet = new SujetPfe();
        sujet.setTitre(dto.getTitre());
        sujet.setDescription(dto.getDescription());
        sujet.setDomaine(dto.getDomaine());
        sujet.setTechnologie(dto.getTechnologie());
        sujet.setEntreprise(dto.getEntreprise());
        sujet.setActif(dto.isActif());

        SujetPfe saved = sujetPfeRepository.save(sujet);
        return mapToDto(saved);
    }

    @Override
    public SujetPfeDto updateSujet(Long id, SujetPfeDto dto) {
        SujetPfe sujet = sujetPfeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Sujet PFE introuvable avec l'id : " + id));

        sujet.setTitre(dto.getTitre());
        sujet.setDescription(dto.getDescription());
        sujet.setDomaine(dto.getDomaine());
        sujet.setTechnologie(dto.getTechnologie());
        sujet.setEntreprise(dto.getEntreprise());
        sujet.setActif(dto.isActif());

        return mapToDto(sujetPfeRepository.save(sujet));
    }

    @Override
    public void deleteSujet(Long id) {
        SujetPfe sujet = sujetPfeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Sujet PFE introuvable avec l'id : " + id));
        sujetPfeRepository.delete(sujet);
    }

    @Override
    public List<SujetPfeDto> getAllSujets() {
        return sujetPfeRepository.findAll().stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    public SujetPfeDto getSujetById(Long id) {
        SujetPfe sujet = sujetPfeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Sujet PFE introuvable avec l'id : " + id));
        return mapToDto(sujet);
    }

    @Override
    public PfeMatchingResponseDto calculateMatching(PfeMatchingRequestDto request) {
        List<SujetPfe> sujets = sujetPfeRepository.findByActifTrue();
        if (sujets.isEmpty()) {
            sujets = sujetPfeRepository.findAll();
        }

        List<String> userSkills = request.getCompetences() == null ? List.of() :
                request.getCompetences().stream()
                        .map(s -> s.trim().toLowerCase(Locale.ROOT))
                        .filter(s -> !s.isEmpty())
                        .collect(Collectors.toList());

        String prefDomaine = request.getDomainePrefere() == null ? "" : request.getDomainePrefere().toLowerCase(Locale.ROOT).trim();

        List<PfeMatchResultDto> results = new ArrayList<>();

        for (SujetPfe sujet : sujets) {
            int score = 40;
            List<String> matchees = new ArrayList<>();

            String sujetTech = sujet.getTechnologie() == null ? "" : sujet.getTechnologie().toLowerCase(Locale.ROOT);
            String sujetDomaine = sujet.getDomaine() == null ? "" : sujet.getDomaine().toLowerCase(Locale.ROOT);
            String sujetText = (sujet.getTitre() + " " + sujet.getDescription()).toLowerCase(Locale.ROOT);

            for (String skill : userSkills) {
                if (sujetTech.contains(skill) || sujetText.contains(skill)) {
                    score += 15;
                    matchees.add(skill);
                }
            }

            if (!prefDomaine.isEmpty() && (sujetDomaine.contains(prefDomaine) || prefDomaine.contains(sujetDomaine))) {
                score += 20;
            }

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
                    .competencesManquantes(List.of())
                    .recommandation(reco)
                    .build());
        }

        results.sort(Comparator.comparingInt(PfeMatchResultDto::getScoreMatch).reversed());
        int topScore = results.isEmpty() ? 0 : results.get(0).getScoreMatch();

        return PfeMatchingResponseDto.builder()
                .candidatNom(request.getCandidatNom() != null ? request.getCandidatNom() : "Candidat")
                .meilleurScore(topScore)
                .totalSujetsAnalyses(results.size())
                .resultats(results)
                .build();
    }

    private SujetPfeDto mapToDto(SujetPfe sujet) {
        return new SujetPfeDto(
                sujet.getId(),
                sujet.getTitre(),
                sujet.getDescription(),
                sujet.getDomaine(),
                sujet.getTechnologie(),
                sujet.getEntreprise(),
                sujet.isActif()
        );
    }
}
