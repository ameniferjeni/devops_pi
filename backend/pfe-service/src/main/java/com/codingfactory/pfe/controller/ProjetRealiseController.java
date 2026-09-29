package com.codingfactory.pfe.controller;

import com.codingfactory.pfe.entity.ProjetRealise;
import com.codingfactory.pfe.repository.ProjetRealiseRepository;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/pfe")
@CrossOrigin(origins = "*")
public class ProjetRealiseController {

    private final ProjetRealiseRepository repository;

    public ProjetRealiseController(ProjetRealiseRepository repository) {
        this.repository = repository;
    }

    @GetMapping("/projets")
    public ResponseEntity<List<ProjetRealise>> getAllProjets() {
        return ResponseEntity.ok(repository.findAll());
    }

    @GetMapping("/projets/{id}")
    public ResponseEntity<ProjetRealise> getProjetById(@PathVariable Long id) {
        return repository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/projets")
    public ResponseEntity<ProjetRealise> createProjet(@Valid @RequestBody ProjetRealise p) {
        return ResponseEntity.status(HttpStatus.CREATED).body(repository.save(p));
    }

    @PutMapping("/projets/{id}")
    public ResponseEntity<ProjetRealise> updateProjet(@PathVariable Long id, @Valid @RequestBody ProjetRealise dto) {
        return repository.findById(id).map(p -> {
            p.setTitre(dto.getTitre());
            p.setDescription(dto.getDescription());
            p.setMethode(dto.getMethode());
            p.setResultat(dto.getResultat());
            return ResponseEntity.ok(repository.save(p));
        }).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/projets/{id}")
    public ResponseEntity<Void> deleteProjet(@PathVariable Long id) {
        repository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
