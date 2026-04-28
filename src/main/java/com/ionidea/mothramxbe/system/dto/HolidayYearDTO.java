package com.ionidea.mothramxbe.system.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class HolidayYearDTO {

    private Integer year;

    private boolean isFinalized;

    private List<HolidayDTO> holidays;

}