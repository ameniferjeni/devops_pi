package com.codingfactory.backend.repository;

import com.codingfactory.backend.entity.Candidature;
import com.codingfactory.backend.entity.Utilisateur;
import com.codingfactory.backend.enums.CandidatureStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CandidatureRepository extends JpaRepository<Candidature, Long> {
    List<Candidature> findByCandidat(Utilisateur candidat);

    long countByStatut(CandidatureStatus statut);
}
