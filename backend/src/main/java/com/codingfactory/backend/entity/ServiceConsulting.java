package com.codingfactory.backend.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "service_consulting")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ServiceConsulting {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Le nom du service est obligatoire")
    @Column(nullable = false, unique = true)
    private String nom;

    @NotBlank(message = "La description est obligatoire")
    @Column(columnDefinition = "TEXT", nullable = false)
    private String description;

    @OneToMany(mappedBy = "serviceConsulting", cascade = CascadeType.ALL)
    private List<Consultant> consultants = new ArrayList<>();
}
