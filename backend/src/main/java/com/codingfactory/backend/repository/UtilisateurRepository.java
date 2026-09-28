package com.codingfactory.backend.repository;

import com.codingfactory.backend.entity.Utilisateur;
import com.codingfactory.backend.enums.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UtilisateurRepository extends JpaRepository<Utilisateur, Long> {
    Optional<Utilisateur> findByEmail(String email);

    List<Utilisateur> findByRoleOrderByNomAsc(Role role);
}
