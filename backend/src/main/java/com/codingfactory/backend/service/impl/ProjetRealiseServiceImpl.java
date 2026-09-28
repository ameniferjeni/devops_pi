package com.codingfactory.backend.service.impl;

import com.codingfactory.backend.dto.ProjetRealiseDto;
import com.codingfactory.backend.entity.ProjetRealise;
import com.codingfactory.backend.exception.ResourceNotFoundException;
import com.codingfactory.backend.repository.ProjetRealiseRepository;
import com.codingfactory.backend.service.ProjetRealiseService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProjetRealiseServiceImpl implements ProjetRealiseService {

    private final ProjetRealiseRepository projetRealiseRepository;

    public ProjetRealiseServiceImpl(ProjetRealiseRepository projetRealiseRepository) {
        this.projetRealiseRepository = projetRealiseRepository;
    }

    @Override
    public ProjetRealiseDto createProjet(ProjetRealiseDto dto) {
        ProjetRealise projet = new ProjetRealise();
        projet.setTitre(dto.getTitre());
        projet.setDescription(dto.getDescription());
        projet.setMethode(dto.getMethode());
        projet.setResultat(dto.getResultat());

        ProjetRealise saved = projetRealiseRepository.save(projet);
        return mapToDto(saved);
    }

    @Override
    public ProjetRealiseDto updateProjet(Long id, ProjetRealiseDto dto) {
        ProjetRealise projet = projetRealiseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Projet réalisé introuvable avec l'id : " + id));

        projet.setTitre(dto.getTitre());
        projet.setDescription(dto.getDescription());
        projet.setMethode(dto.getMethode());
        projet.setResultat(dto.getResultat());

        return mapToDto(projetRealiseRepository.save(projet));
    }

    @Override
    public void deleteProjet(Long id) {
        ProjetRealise projet = projetRealiseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Projet réalisé introuvable avec l'id : " + id));
        projetRealiseRepository.delete(projet);
    }

    @Override
    public List<ProjetRealiseDto> getAllProjets() {
        return projetRealiseRepository.findAll().stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    public ProjetRealiseDto getProjetById(Long id) {
        ProjetRealise projet = projetRealiseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Projet réalisé introuvable avec l'id : " + id));
        return mapToDto(projet);
    }

    private ProjetRealiseDto mapToDto(ProjetRealise projet) {
        return new ProjetRealiseDto(
                projet.getId(),
                projet.getTitre(),
                projet.getDescription(),
                projet.getMethode(),
                projet.getResultat()
        );
    }
}
