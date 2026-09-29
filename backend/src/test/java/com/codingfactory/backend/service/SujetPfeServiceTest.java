package com.codingfactory.backend.service;

import com.codingfactory.backend.dto.PfeMatchingRequestDto;
import com.codingfactory.backend.dto.PfeMatchingResponseDto;
import com.codingfactory.backend.repository.SujetPfeRepository;
import com.codingfactory.backend.service.impl.SujetPfeServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SujetPfeServiceTest {

    @Mock
    private SujetPfeRepository sujetPfeRepository;

    @InjectMocks
    private SujetPfeServiceImpl sujetPfeService;

    @Test
    @DisplayName("Should match candidate skills with active PFE subjects")
    void testCalculateMatching() {
        when(sujetPfeRepository.findByActifTrue()).thenReturn(Collections.emptyList());

        PfeMatchingRequestDto request = PfeMatchingRequestDto.builder()
                .candidatNom("Salma Mansouri")
                .competences(List.of("Java", "Spring Boot", "Angular"))
                .domainePrefere("DevOps")
                .niveauEtudes("Ingénieur Software")
                .build();

        PfeMatchingResponseDto response = sujetPfeService.calculateMatching(request);

        assertNotNull(response);
        assertNotNull(response.getResultats());
        assertEquals("Salma Mansouri", response.getCandidatNom());
    }
}
