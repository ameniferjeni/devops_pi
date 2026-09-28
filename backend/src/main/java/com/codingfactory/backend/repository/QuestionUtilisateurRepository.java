package com.codingfactory.backend.repository;

import com.codingfactory.backend.entity.QuestionUtilisateur;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface QuestionUtilisateurRepository extends JpaRepository<QuestionUtilisateur, Long> {
}
