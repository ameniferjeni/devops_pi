package com.codingfactory.backend.dto;

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
    private String service;
    private String serviceDescription;
    private String consultant;
    private String consultantEmail;
}
