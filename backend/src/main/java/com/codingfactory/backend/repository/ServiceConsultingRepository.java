package com.codingfactory.backend.repository;

import com.codingfactory.backend.entity.ServiceConsulting;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ServiceConsultingRepository extends JpaRepository<ServiceConsulting, Long> {
    Optional<ServiceConsulting> findByNomIgnoreCase(String nom);
}
