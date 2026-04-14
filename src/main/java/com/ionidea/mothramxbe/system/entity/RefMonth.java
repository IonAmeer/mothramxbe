package com.ionidea.mothramxbe.system.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "ref_month")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RefMonth {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String label;

    private String month;

    private Integer year;

}