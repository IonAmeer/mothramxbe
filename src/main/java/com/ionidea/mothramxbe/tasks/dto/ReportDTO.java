package com.ionidea.mothramxbe.tasks.dto;

import lombok.Data;

@Data
public class ReportDTO {

    // ---------------- BASIC FIELDS ----------------
    private Long id;

    private Long userId;

    private String developerName;

    private String status;

    private String approvedBy;

    private String rejectionReason;

    private Long refMonthId;

    // ---------------- AGGREGATED FIELDS ----------------
    private int sp;

    private int taken;

    private int wd;

}