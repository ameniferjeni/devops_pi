package com.codingfactory.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuthRequestDto {
    private String email;
    private String password;
    private String nom;
    private String prenom;
    private String role; // "ADMIN" or "CANDIDAT"
}
