package com.codingfactory.backend.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "sujet_pfe")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SujetPfe {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Le titre est obligatoire")
    @Column(nullable = false)
    private String titre;

    @NotBlank(message = "La description est obligatoire")
    @Column(columnDefinition = "TEXT", nullable = false)
    private String description;

    @NotBlank(message = "Le domaine est obligatoire")
    @Column(nullable = false)
    private String domaine;

    @NotBlank(message = "La technologie est obligatoire")
    @Column(nullable = false)
    private String technologie;

    @Column(nullable = false)
    private String entreprise;

    @Column(nullable = false)
    private LocalDateTime datePublication = LocalDateTime.now();

    @Column(nullable = false)
    private boolean actif = true;

    @OneToMany(mappedBy = "sujetPfe", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Candidature> candidatures = new ArrayList<>();
}
