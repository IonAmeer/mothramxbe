package com.ionidea.mothramxbe.system.repository;

import com.ionidea.mothramxbe.system.entity.Holiday;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface HolidayRepository extends JpaRepository<Holiday, Long> {

    // ✅ FOR DUPLICATE CHECK
    Optional<Holiday> findByYearAndMonth_IdAndDay(Integer year, Long monthId, Integer day);

    // ✅ ADD THIS (FIX YOUR ERROR)
    List<Holiday> findByYear(Integer year);

    // ✅ ADD THIS (IMPORTANT)
    List<Holiday> findByYearAndMonth_Id(Integer year, Long monthId);

}