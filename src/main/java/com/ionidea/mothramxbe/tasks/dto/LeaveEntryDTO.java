package com.ionidea.mothramxbe.tasks.dto;

import lombok.Data;

@Data
public class LeaveEntryDTO {

    private Long id;

    private String date;

    private String duration;

    private String reason;

    private Long leaveTypeId;

    private Long reportId;

}