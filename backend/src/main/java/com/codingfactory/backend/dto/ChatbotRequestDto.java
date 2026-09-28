package com.codingfactory.backend.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChatbotRequestDto {

    @NotBlank(message = "La question est obligatoire")
    private String question;

    /** Prénom de l'utilisateur pour personnaliser la réponse (optionnel). */
    private String prenom;
}
