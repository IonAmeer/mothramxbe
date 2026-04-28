package com.ionidea.mothramxbe.tasks.repository;

import com.ionidea.mothramxbe.tasks.model.Report;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.NativeQuery;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

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
    int save(@Param("id") Long id,
             @Param("status") String status);

    @Query("""
                SELECT r
                FROM Report r
                JOIN FETCH r.user u
                JOIN LeadTeam lt ON u.id = lt.developer.id
                WHERE lt.lead.id = :leadId
                AND r.refMonthId = :monthId
            """)
    List<Report> findByLeadAndMonth(@Param("leadId") Long leadId,
                                    @Param("monthId") Long monthId);

    @Query("""
                SELECT r
                FROM Report r
                JOIN FETCH r.user u
                JOIN LeadTeam lt ON u.id = lt.developer.id
                WHERE lt.lead.id = :leadId
                AND u.id = :developerId
                AND r.refMonthId = :monthId
            """)
    List<Report> findByDeveloperLeadAndMonth(
            @Param("developerId") Long developerId,
            @Param("leadId") Long leadId,
            @Param("monthId") Long monthId);

    Optional<Report> findByUserIdAndRefMonthId(Long userId, Long refMonthId);

}