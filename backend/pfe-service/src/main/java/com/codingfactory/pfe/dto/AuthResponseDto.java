package com.codingfactory.pfe.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuthResponseDto {
    private String token;
    private Long id;
    private String email;
    private String nom;
    private String prenom;
    private String role;
    private String message;
}
