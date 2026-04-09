package com.ionidea.mothramxbe.tasks.repository;

import com.ionidea.mothramxbe.tasks.model.Report;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReportRepository extends JpaRepository<Report, Integer> {

    List<Report> findByUser_Lead_IdAndRefMonthId(Integer leadId, Integer monthId);

    List<Report> findByUserIdAndUser_Lead_Id(Integer userId, Integer leadId);

    List<Report> findByUserIdAndUser_Lead_IdAndRefMonthId(Integer userId, Integer leadId, Integer refMonthId);

    List<Report> findByUser_Lead_Id(Integer leadId);

}