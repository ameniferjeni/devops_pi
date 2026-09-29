package com.codingfactory.pfe.repository;

import com.codingfactory.pfe.entity.SujetPfe;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SujetPfeRepository extends JpaRepository<SujetPfe, Long> {
    List<SujetPfe> findByActifTrue();
    long countByActifTrue();
}
