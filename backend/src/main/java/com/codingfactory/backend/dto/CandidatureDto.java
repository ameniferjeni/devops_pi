package com.codingfactory.backend.dto;

import com.codingfactory.backend.enums.CandidatureStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CandidatureDto {

    private Long id;

    @NotNull(message = "L'identifiant du sujet est obligatoire")
    private Long sujetPfeId;

    @NotNull(message = "L'identifiant du candidat est obligatoire")
    private Long candidatId;

    @NotBlank(message = "Le message de motivation est obligatoire")
    private String messageMotivation;

    private CandidatureStatus statut;
}
