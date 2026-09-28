package com.codingfactory.backend.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProjetRealiseDto {

    private Long id;

    @NotBlank(message = "Le titre est obligatoire")
    private String titre;

    @NotBlank(message = "La description est obligatoire")
    private String description;

    @NotBlank(message = "La méthodologie est obligatoire")
    private String methode;

    @NotBlank(message = "Le résultat est obligatoire")
    private String resultat;
}
