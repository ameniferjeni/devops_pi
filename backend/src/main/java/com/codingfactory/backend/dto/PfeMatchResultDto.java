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
public class PfeMatchResultDto {
    private Long sujetId;
    private String titre;
    private String domaine;
    private String technologie;
    private String entreprise;
    private int scoreMatch;
    private String statutMatch;
    private List<String> competencesMatchees;
    private List<String> competencesManquantes;
    private String recommandation;
}
