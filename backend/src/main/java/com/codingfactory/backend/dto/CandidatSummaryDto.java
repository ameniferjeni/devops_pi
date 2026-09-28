package com.codingfactory.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CandidatSummaryDto {
    private Long id;
    private String nom;
    private String prenom;
    private String email;
}
