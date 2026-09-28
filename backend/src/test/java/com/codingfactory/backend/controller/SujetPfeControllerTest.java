package com.codingfactory.backend.controller;

import com.codingfactory.backend.dto.SujetPfeDto;
import com.codingfactory.backend.service.SujetPfeService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@WebMvcTest(SujetPfeController.class)
class SujetPfeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private SujetPfeService sujetPfeService;

    @Test
    void shouldReturnListOfSujets() throws Exception {
        SujetPfeDto dto = new SujetPfeDto(1L, "PFE Java", "Projet backend", "Informatique", "Spring Boot", "CodingFactory", true);
        when(sujetPfeService.getAllSujets()).thenReturn(List.of(dto));

        mockMvc.perform(get("/api/pfe/sujets")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].titre").value("PFE Java"));
    }
}
