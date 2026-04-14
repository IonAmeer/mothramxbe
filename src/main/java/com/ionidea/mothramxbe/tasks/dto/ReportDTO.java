package com.ionidea.mothramxbe.tasks.dto;

import lombok.Data;

@Data
public class ReportDTO {

    private Long id;

    private Long userId;

    private String status;

    private String approvedBy;

    private String rejectionReason;

    private Long refMonthId;

}