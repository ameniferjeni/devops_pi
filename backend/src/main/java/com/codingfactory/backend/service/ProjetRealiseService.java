package com.codingfactory.backend.service;

import com.codingfactory.backend.dto.ProjetRealiseDto;

import java.util.List;

public interface ProjetRealiseService {
    ProjetRealiseDto createProjet(ProjetRealiseDto dto);
    ProjetRealiseDto updateProjet(Long id, ProjetRealiseDto dto);
    void deleteProjet(Long id);
    List<ProjetRealiseDto> getAllProjets();
    ProjetRealiseDto getProjetById(Long id);
}
