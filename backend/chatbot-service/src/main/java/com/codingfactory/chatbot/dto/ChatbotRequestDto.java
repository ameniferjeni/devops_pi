package com.codingfactory.chatbot.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChatbotRequestDto {
    private String question;
    private String prenom;
    private String entreprise;
    private String role;
}
