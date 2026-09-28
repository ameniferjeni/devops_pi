package com.codingfactory.backend.controller;

import com.codingfactory.backend.dto.CandidatSummaryDto;
import com.codingfactory.backend.entity.Utilisateur;
import com.codingfactory.backend.enums.Role;
import com.codingfactory.backend.repository.UtilisateurRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/pfe")
@CrossOrigin(origins = "*")
public class CandidatController {

    private final UtilisateurRepository utilisateurRepository;

    public CandidatController(UtilisateurRepository utilisateurRepository) {
        this.utilisateurRepository = utilisateurRepository;
    }

    @GetMapping("/candidats")
    public ResponseEntity<List<CandidatSummaryDto>> listCandidats() {
        List<CandidatSummaryDto> candidats = utilisateurRepository.findByRoleOrderByNomAsc(Role.CANDIDAT).stream()
                .map(this::toDto)
                .toList();
        return ResponseEntity.ok(candidats);
    }

    private CandidatSummaryDto toDto(Utilisateur u) {
        return new CandidatSummaryDto(u.getId(), u.getNom(), u.getPrenom(), u.getEmail());
    }
}
