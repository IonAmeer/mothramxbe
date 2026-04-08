package com.ionidea.mothramxbe.system.dto;

import lombok.Data;

@Data
public class HolidayRequestDTO {

    private String name;

    private int day;

    private int year;

    private Long monthId;

}