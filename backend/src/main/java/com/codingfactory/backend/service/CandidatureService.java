package com.codingfactory.backend.service;

import com.codingfactory.backend.dto.CandidatureDto;
import com.codingfactory.backend.dto.CandidatureSubmitDto;

import java.util.List;

public interface CandidatureService {
    CandidatureDto createCandidature(CandidatureDto dto);

    CandidatureDto submitCandidature(CandidatureSubmitDto dto);
    CandidatureDto updateStatus(Long id, String status);
    List<CandidatureDto> getAllCandidatures();
    List<CandidatureDto> getCandidaturesByCandidat(Long candidatId);
    CandidatureDto getById(Long id);
}
