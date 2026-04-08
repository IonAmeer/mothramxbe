package com.ionidea.mothramxbe.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class HolidayResponseDTO {
    private Long id;
    private String name;
    private int day;
    private int year;
    private String month;
}