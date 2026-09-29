package com.codingfactory.chatbot.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChatbotResponseDto {
    private String intention;
    private String intentionLabel;
    private String reponse;
    private String serviceNom;
    private String serviceDescription;
    private String consultantNom;
    private String consultantEmail;
}
