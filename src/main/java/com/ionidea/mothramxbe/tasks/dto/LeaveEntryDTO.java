package com.ionidea.mothramxbe.tasks.dto;

import lombok.Data;

@Data
public class LeaveEntryDTO {

    private Integer id;

    private String date;

    private String duration;

    private String reason;

    private Integer leaveTypeId;

    private Integer reportId;

}