package com.ionidea.mothramxbe.system.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "holiday_year")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class HolidayYear {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private Integer year;

    @Column(nullable = false)
    private boolean isFinalized = false;

    @OneToMany(mappedBy = "holidayYear", cascade = CascadeType.ALL)
    private List<Holiday> holidays;

}