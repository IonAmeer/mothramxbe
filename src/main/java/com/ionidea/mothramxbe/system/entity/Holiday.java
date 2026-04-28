package com.ionidea.mothramxbe.system.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "holiday")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Holiday {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String holidayName;

    private LocalDate holidayDate;

    @ManyToOne
    @JoinColumn(name = "year_id", nullable = false)
    private HolidayYear holidayYear;

}