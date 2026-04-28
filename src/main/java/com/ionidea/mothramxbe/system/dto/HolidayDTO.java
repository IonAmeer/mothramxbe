package com.ionidea.mothramxbe.system.dto;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class HolidayDTO {

    private Long id;

    private String holidayName;

    private LocalDate holidayDate;

}