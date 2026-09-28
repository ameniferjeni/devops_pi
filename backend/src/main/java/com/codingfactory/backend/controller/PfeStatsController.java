package com.codingfactory.backend.controller;

import com.codingfactory.backend.dto.PfeStatsDto;
import com.codingfactory.backend.enums.CandidatureStatus;
import com.codingfactory.backend.repository.CandidatureRepository;
import com.codingfactory.backend.repository.ProjetRealiseRepository;
import com.codingfactory.backend.repository.SujetPfeRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/pfe")
@CrossOrigin(origins = "*")
public class PfeStatsController {

    private final SujetPfeRepository sujetPfeRepository;
    private final ProjetRealiseRepository projetRealiseRepository;
    private final CandidatureRepository candidatureRepository;

    public PfeStatsController(SujetPfeRepository sujetPfeRepository,
                              ProjetRealiseRepository projetRealiseRepository,
                              CandidatureRepository candidatureRepository) {
        this.sujetPfeRepository = sujetPfeRepository;
        this.projetRealiseRepository = projetRealiseRepository;
        this.candidatureRepository = candidatureRepository;
    }

    @GetMapping("/stats")
    public ResponseEntity<PfeStatsDto> stats() {
        return ResponseEntity.ok(new PfeStatsDto(
                sujetPfeRepository.count(),
                sujetPfeRepository.countByActifTrue(),
                projetRealiseRepository.count(),
                candidatureRepository.countByStatut(CandidatureStatus.EN_ATTENTE)
        ));
    }
}
