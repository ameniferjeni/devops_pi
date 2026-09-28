package com.codingfactory.backend.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SujetPfeDto {

    private Long id;

    @NotBlank(message = "Le titre est obligatoire")
    private String titre;

    @NotBlank(message = "La description est obligatoire")
    private String description;

    @NotBlank(message = "Le domaine est obligatoire")
    private String domaine;

    @NotBlank(message = "La technologie est obligatoire")
    private String technologie;

    @NotBlank(message = "L'entreprise est obligatoire")
    private String entreprise;

    private boolean actif;
}
