package com.codingfactory.pfe.controller;

import com.codingfactory.pfe.entity.SujetPfe;
import com.codingfactory.pfe.repository.SujetPfeRepository;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/pfe")
public class SujetPfeController {

    private final SujetPfeRepository repo;

    public SujetPfeController(SujetPfeRepository repo) { this.repo = repo; }

    @GetMapping("/sujets")
    public List<SujetPfe> list() { return repo.findAll(); }

    @GetMapping("/sujets/actifs")
    public List<SujetPfe> listActifs() { return repo.findByActifTrue(); }

    @PostMapping("/sujets")
    public ResponseEntity<SujetPfe> create(@Valid @RequestBody SujetPfe s) {
        return ResponseEntity.status(HttpStatus.CREATED).body(repo.save(s));
    }

    @PutMapping("/sujets/{id}")
    public ResponseEntity<SujetPfe> update(@PathVariable Long id, @Valid @RequestBody SujetPfe dto) {
        return repo.findById(id).map(sujet -> {
            sujet.setTitre(dto.getTitre());
            sujet.setDescription(dto.getDescription());
            sujet.setDomaine(dto.getDomaine());
            sujet.setTechnologie(dto.getTechnologie());
            sujet.setEntreprise(dto.getEntreprise());
            sujet.setActif(dto.isActif());
            return ResponseEntity.ok(repo.save(sujet));
        }).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/sujets/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        if (!repo.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        repo.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
