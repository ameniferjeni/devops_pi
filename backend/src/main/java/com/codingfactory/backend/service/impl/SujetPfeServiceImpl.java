package com.codingfactory.backend.service.impl;

import com.codingfactory.backend.dto.SujetPfeDto;
import com.codingfactory.backend.entity.SujetPfe;
import com.codingfactory.backend.exception.ResourceNotFoundException;
import com.codingfactory.backend.repository.SujetPfeRepository;
import com.codingfactory.backend.service.SujetPfeService;
import org.springframework.stereotype.Service;

import java.util.List;
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
