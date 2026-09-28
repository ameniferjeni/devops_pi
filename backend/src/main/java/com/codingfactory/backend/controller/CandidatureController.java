package com.codingfactory.backend.controller;

import com.codingfactory.backend.dto.CandidatureDto;
import com.codingfactory.backend.dto.CandidatureSubmitDto;
import com.codingfactory.backend.service.CandidatureService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/pfe")
@CrossOrigin(origins = "*")
public class CandidatureController {

    private final CandidatureService candidatureService;

    public CandidatureController(CandidatureService candidatureService) {
        this.candidatureService = candidatureService;
    }

    @GetMapping("/candidatures")
    public ResponseEntity<List<CandidatureDto>> getAllCandidatures() {
        return ResponseEntity.ok(candidatureService.getAllCandidatures());
    }

    @GetMapping("/candidatures/{id}")
    public ResponseEntity<CandidatureDto> getById(@PathVariable Long id) {
        return ResponseEntity.ok(candidatureService.getById(id));
    }

    @GetMapping("/candidatures/candidat/{candidatId}")
    public ResponseEntity<List<CandidatureDto>> getByCandidat(@PathVariable Long candidatId) {
        return ResponseEntity.ok(candidatureService.getCandidaturesByCandidat(candidatId));
    }

    @PostMapping("/candidatures")
    public ResponseEntity<CandidatureDto> createCandidature(@Valid @RequestBody CandidatureDto dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(candidatureService.createCandidature(dto));
    }

    @PostMapping("/candidatures/submit")
    public ResponseEntity<CandidatureDto> submitCandidature(@Valid @RequestBody CandidatureSubmitDto dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(candidatureService.submitCandidature(dto));
    }

    @PatchMapping("/candidatures/{id}/status")
    public ResponseEntity<CandidatureDto> updateStatus(@PathVariable Long id, @RequestParam String status) {
        return ResponseEntity.ok(candidatureService.updateStatus(id, status));
    }
}
