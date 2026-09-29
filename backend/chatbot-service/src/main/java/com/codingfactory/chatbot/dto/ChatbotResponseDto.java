package com.codingfactory.chatbot.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChatbotResponseDto {
    private String intention;
    private String intentionLabel;
    private String reponse;
    private String serviceNom;
    private String serviceDescription;
    private String consultantNom;
    private String consultantRole;
    private String consultantEmail;
    private String consultantPhone;
    private String consultantAvatar;
    private List<String> suggestions;
    private String actionType;

    public ChatbotResponseDto(String intention, String intentionLabel, String reponse,
                              String serviceNom, String serviceDescription,
                              String consultantNom, String consultantEmail) {
        this.intention = intention;
        this.intentionLabel = intentionLabel;
        this.reponse = reponse;
        this.serviceNom = serviceNom;
        this.serviceDescription = serviceDescription;
        this.consultantNom = consultantNom;
        this.consultantEmail = consultantEmail;
    }

    public String getService() {
        return serviceNom;
    }

    public String getConsultant() {
        return consultantNom;
    }
}
