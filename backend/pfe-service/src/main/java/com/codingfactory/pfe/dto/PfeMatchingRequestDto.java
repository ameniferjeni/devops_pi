package com.codingfactory.pfe.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PfeMatchingRequestDto {
    private String candidatNom;
    private List<String> competences;
    private String domainePrefere;
    private String niveauEtudes;
}
