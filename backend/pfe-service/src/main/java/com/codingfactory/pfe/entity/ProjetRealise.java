package com.codingfactory.pfe.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "projet_realise")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProjetRealise {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Le titre est obligatoire")
    @Column(nullable = false)
    private String titre;

    @NotBlank(message = "La description est obligatoire")
    @Column(columnDefinition = "TEXT", nullable = false)
    private String description;

    @NotBlank(message = "La méthodologie est obligatoire")
    @Column(columnDefinition = "TEXT", nullable = false)
    private String methode;

    @NotBlank(message = "Le résultat est obligatoire")
    @Column(columnDefinition = "TEXT", nullable = false)
    private String resultat;

    @Column(nullable = false)
    private LocalDateTime dateRealisation = LocalDateTime.now();
}
