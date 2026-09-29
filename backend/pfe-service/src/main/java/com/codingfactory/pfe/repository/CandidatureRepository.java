package com.codingfactory.pfe.repository;

import com.codingfactory.pfe.entity.Candidature;
import com.codingfactory.pfe.enums.CandidatureStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CandidatureRepository extends JpaRepository<Candidature, Long> {
    List<Candidature> findByCandidatId(Long candidatId);
    List<Candidature> findBySujetPfeId(Long sujetPfeId);
    long countByCandidatId(Long candidatId);
    long countBySujetPfeId(Long sujetPfeId);
    long countByStatut(CandidatureStatus statut);
}
