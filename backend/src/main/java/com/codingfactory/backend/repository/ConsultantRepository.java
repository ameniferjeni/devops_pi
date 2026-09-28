package com.codingfactory.backend.repository;

import com.codingfactory.backend.entity.Consultant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ConsultantRepository extends JpaRepository<Consultant, Long> {
    List<Consultant> findByServiceConsultingId(Long serviceId);
}
