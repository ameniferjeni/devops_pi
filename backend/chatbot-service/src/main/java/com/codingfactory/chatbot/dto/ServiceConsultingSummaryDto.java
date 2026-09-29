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
public class ServiceConsultingSummaryDto {
    private Long id;
    private String nom;
    private String description;
    private int consultantsCount;
    private String expertNom;
    private String expertRole;
    private String expertEmail;
    private String expertPhone;
    private List<String> technologies;

    public ServiceConsultingSummaryDto(Long id, String nom, String description, int consultantsCount) {
        this.id = id;
        this.nom = nom;
        this.description = description;
        this.consultantsCount = consultantsCount;
    }
}
