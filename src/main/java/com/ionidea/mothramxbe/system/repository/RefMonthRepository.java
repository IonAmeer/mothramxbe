package com.ionidea.mothramxbe.system.repository;

import com.ionidea.mothramxbe.system.entity.RefMonth;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RefMonthRepository extends JpaRepository<RefMonth, Long> {

    Optional<RefMonth> findByMonthAndYear(String month, Integer year);

}