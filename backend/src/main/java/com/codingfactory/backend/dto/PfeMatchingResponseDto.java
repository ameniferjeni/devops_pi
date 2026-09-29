package com.codingfactory.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PfeMatchingResponseDto {
    private String candidatNom;
    private int meilleurScore;
    private int totalSujetsAnalyses;
    private List<PfeMatchResultDto> resultats;
}
