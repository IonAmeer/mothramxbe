package com.ionidea.mothramxbe.system.repository;

import com.ionidea.mothramxbe.system.entity.Holiday;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface HolidayRepository extends JpaRepository<Holiday, Long> {

    Optional<Holiday> findByHolidayDateAndHolidayYear_Year(LocalDate date, Integer year);

//    List<Holiday> findByYearAndMonth_Id(Integer year, Long monthId);

}