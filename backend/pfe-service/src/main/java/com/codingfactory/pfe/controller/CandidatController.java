package com.codingfactory.pfe.controller;

import com.codingfactory.pfe.entity.Utilisateur;
import com.codingfactory.pfe.enums.Role;
import com.codingfactory.pfe.repository.UtilisateurRepository;
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
    public ResponseEntity<List<Utilisateur>> listCandidats() {
        return ResponseEntity.ok(utilisateurRepository.findByRole(Role.CANDIDAT));
    }
}
