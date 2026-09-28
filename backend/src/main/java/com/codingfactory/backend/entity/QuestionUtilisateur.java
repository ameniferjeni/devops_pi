package com.codingfactory.backend.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "question_utilisateur")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class QuestionUtilisateur {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "La question est obligatoire")
    @Column(nullable = false, columnDefinition = "TEXT")
    private String contenu;

    @Column(nullable = false)
    private LocalDateTime dateQuestion = LocalDateTime.now();

    @Column(nullable = false)
    private String intention;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "service_consulting_id")
    private ServiceConsulting serviceConsulting;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "consultant_id")
    private Consultant consultant;
}
