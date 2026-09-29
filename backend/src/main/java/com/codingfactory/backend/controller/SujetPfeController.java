package com.codingfactory.backend.controller;

import com.codingfactory.backend.dto.PfeMatchingRequestDto;
import com.codingfactory.backend.dto.PfeMatchingResponseDto;
import com.codingfactory.backend.dto.SujetPfeDto;
import com.codingfactory.backend.service.SujetPfeService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/pfe")
@CrossOrigin(origins = "*")
public class SujetPfeController {

    private final SujetPfeService sujetPfeService;

    public SujetPfeController(SujetPfeService sujetPfeService) {
        this.sujetPfeService = sujetPfeService;
    }

    @GetMapping("/sujets")
    public ResponseEntity<List<SujetPfeDto>> getAllSujets() {
        return ResponseEntity.ok(sujetPfeService.getAllSujets());
    }

    @PostMapping("/sujets/match")
    public ResponseEntity<PfeMatchingResponseDto> calculateMatching(@RequestBody PfeMatchingRequestDto request) {
        return ResponseEntity.ok(sujetPfeService.calculateMatching(request));
    }

    @GetMapping("/sujets/{id}")
    public ResponseEntity<SujetPfeDto> getSujetById(@PathVariable Long id) {
        return ResponseEntity.ok(sujetPfeService.getSujetById(id));
    }

    @PostMapping("/sujets")
    public ResponseEntity<SujetPfeDto> createSujet(@Valid @RequestBody SujetPfeDto dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(sujetPfeService.createSujet(dto));
    }

    @PutMapping("/sujets/{id}")
    public ResponseEntity<SujetPfeDto> updateSujet(@PathVariable Long id, @Valid @RequestBody SujetPfeDto dto) {
        return ResponseEntity.ok(sujetPfeService.updateSujet(id, dto));
    }

    @DeleteMapping("/sujets/{id}")
    public ResponseEntity<Void> deleteSujet(@PathVariable Long id) {
        sujetPfeService.deleteSujet(id);
        return ResponseEntity.noContent().build();
    }
}
