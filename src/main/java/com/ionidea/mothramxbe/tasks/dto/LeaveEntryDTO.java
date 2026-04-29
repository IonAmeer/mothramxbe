package com.ionidea.mothramxbe.tasks.dto;

import lombok.*;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
public class LeaveEntryDTO {

    private Long id;

    private String date;

    private String duration;

    private String reason;

    private Long leaveTypeId;

    private String leaveType;

    private Long reportId;

}