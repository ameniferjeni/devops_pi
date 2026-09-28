package com.codingfactory.backend.service;

import com.codingfactory.backend.dto.SujetPfeDto;
import com.codingfactory.backend.entity.SujetPfe;
import com.codingfactory.backend.repository.SujetPfeRepository;
import com.codingfactory.backend.service.impl.SujetPfeServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SujetPfeServiceTest {

    @Mock
    private SujetPfeRepository sujetPfeRepository;

    @InjectMocks
    private SujetPfeServiceImpl sujetPfeService;

    @Test
    void shouldCreateSujet() {
        SujetPfeDto dto = new SujetPfeDto(null, "PFE Java", "Projet backend", "Informatique", "Spring Boot", "CodingFactory", true);
        SujetPfe sujet = new SujetPfe(1L, "PFE Java", "Projet backend", "Informatique", "Spring Boot", "CodingFactory", null, true, null);

        when(sujetPfeRepository.save(any(SujetPfe.class))).thenReturn(sujet);

        SujetPfeDto result = sujetPfeService.createSujet(dto);

        assertNotNull(result);
        assertEquals("PFE Java", result.getTitre());
        verify(sujetPfeRepository, times(1)).save(any(SujetPfe.class));
    }

    @Test
    void shouldGetAllSujets() {
        SujetPfe sujet = new SujetPfe(1L, "PFE Java", "Projet backend", "Informatique", "Spring Boot", "CodingFactory", null, true, null);
        when(sujetPfeRepository.findAll()).thenReturn(List.of(sujet));

        List<SujetPfeDto> result = sujetPfeService.getAllSujets();

        assertEquals(1, result.size());
        assertEquals("PFE Java", result.get(0).getTitre());
    }
}
