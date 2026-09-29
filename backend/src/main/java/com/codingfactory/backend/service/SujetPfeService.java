package com.codingfactory.backend.service;

import com.codingfactory.backend.dto.PfeMatchingRequestDto;
import com.codingfactory.backend.dto.PfeMatchingResponseDto;
import com.codingfactory.backend.dto.SujetPfeDto;

import java.util.List;

public interface SujetPfeService {
    SujetPfeDto createSujet(SujetPfeDto dto);
    SujetPfeDto updateSujet(Long id, SujetPfeDto dto);
    void deleteSujet(Long id);
    List<SujetPfeDto> getAllSujets();
    SujetPfeDto getSujetById(Long id);
    PfeMatchingResponseDto calculateMatching(PfeMatchingRequestDto request);
}
