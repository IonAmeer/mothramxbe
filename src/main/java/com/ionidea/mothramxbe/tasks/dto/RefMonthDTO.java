package com.ionidea.mothramxbe.tasks.dto;

import lombok.*;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
public class RefMonthDTO {

    private Long id;

    private String month;

    private Integer year;

}