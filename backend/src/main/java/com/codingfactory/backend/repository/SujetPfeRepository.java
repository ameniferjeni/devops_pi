package com.codingfactory.backend.repository;

import com.codingfactory.backend.entity.SujetPfe;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SujetPfeRepository extends JpaRepository<SujetPfe, Long> {
    long countByActifTrue();
    List<SujetPfe> findByActifTrue();
}
