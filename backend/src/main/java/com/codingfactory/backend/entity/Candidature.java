package com.codingfactory.backend.entity;

import com.codingfactory.backend.enums.CandidatureStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "candidature")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Candidature {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sujet_pfe_id", nullable = false)
    private SujetPfe sujetPfe;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "candidat_id", nullable = false)
    private Utilisateur candidat;

    @NotBlank(message = "Le message de motivation est obligatoire")
    @Column(columnDefinition = "TEXT", nullable = false)
    private String messageMotivation;

    @Column(nullable = false)
    private LocalDateTime dateSoumission = LocalDateTime.now();

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CandidatureStatus statut = CandidatureStatus.EN_ATTENTE;
}
