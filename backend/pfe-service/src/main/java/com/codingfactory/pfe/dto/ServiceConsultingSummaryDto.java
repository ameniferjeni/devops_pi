package com.codingfactory.pfe.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ServiceConsultingSummaryDto {
    private Long id;
    private String nom;
    private String description;
    private int consultantsCount;
}
