package com.codingfactory.pfe.repository;

import com.codingfactory.pfe.entity.ProjetRealise;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProjetRealiseRepository extends JpaRepository<ProjetRealise, Long> {
}
