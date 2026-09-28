package com.codingfactory.backend.controller;

import com.codingfactory.backend.dto.ProjetRealiseDto;
import com.codingfactory.backend.service.ProjetRealiseService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/pfe")
@CrossOrigin(origins = "*")
public class ProjetRealiseController {

    private final ProjetRealiseService projetRealiseService;

    public ProjetRealiseController(ProjetRealiseService projetRealiseService) {
        this.projetRealiseService = projetRealiseService;
    }

    @GetMapping("/projets")
    public ResponseEntity<List<ProjetRealiseDto>> getAllProjets() {
        return ResponseEntity.ok(projetRealiseService.getAllProjets());
    }

    @GetMapping("/projets/{id}")
    public ResponseEntity<ProjetRealiseDto> getProjetById(@PathVariable Long id) {
        return ResponseEntity.ok(projetRealiseService.getProjetById(id));
    }

    @PostMapping("/projets")
    public ResponseEntity<ProjetRealiseDto> createProjet(@Valid @RequestBody ProjetRealiseDto dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(projetRealiseService.createProjet(dto));
    }

    @PutMapping("/projets/{id}")
    public ResponseEntity<ProjetRealiseDto> updateProjet(@PathVariable Long id, @Valid @RequestBody ProjetRealiseDto dto) {
        return ResponseEntity.ok(projetRealiseService.updateProjet(id, dto));
    }

    @DeleteMapping("/projets/{id}")
    public ResponseEntity<Void> deleteProjet(@PathVariable Long id) {
        projetRealiseService.deleteProjet(id);
        return ResponseEntity.noContent().build();
    }
}
