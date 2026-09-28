package com.codingfactory.backend.repository;

import com.codingfactory.backend.entity.ProjetRealise;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProjetRealiseRepository extends JpaRepository<ProjetRealise, Long> {
}
