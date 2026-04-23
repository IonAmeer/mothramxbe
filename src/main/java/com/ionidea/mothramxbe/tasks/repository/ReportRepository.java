package com.ionidea.mothramxbe.tasks.repository;

import com.ionidea.mothramxbe.tasks.model.Report;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.NativeQuery;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ReportRepository extends JpaRepository<Report, Long> {

    @Query(value = """
                SELECT r.*
                FROM reports r
                JOIN lead_team lt ON r.user_id = lt.developer_id
                WHERE lt.lead_id = :leadId
                AND r.ref_month_id = :monthId
            """, nativeQuery = true)
    List<Report> findReportByLead(@Param("leadId") Long leadId,
                                  @Param("monthId") Long monthId);

    @NativeQuery(value = "UPDATE report SET status = :status WHERE id = :id")
    int updateStatus(@Param("id") Long id,
                     @Param("status") String status);

}