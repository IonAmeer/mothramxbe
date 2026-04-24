package com.ionidea.mothramxbe.system.repository;

import com.ionidea.mothramxbe.system.entity.HolidayYear;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface HolidayYearRepository extends JpaRepository<HolidayYear, Long> {

    Optional<HolidayYear> findByYear(Integer year);

}