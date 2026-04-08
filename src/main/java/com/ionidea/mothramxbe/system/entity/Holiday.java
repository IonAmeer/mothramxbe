package com.ionidea.mothramxbe.system.entity;

import jakarta.persistence.*;

@Entity
@Table(
        name = "holidays",
        uniqueConstraints = @UniqueConstraint(columnNames = {"year", "month_id", "day"})
)
public class Holiday {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private Integer day;

    private Integer year;

    @ManyToOne
    @JoinColumn(name = "month_id", nullable = false)
    private RefMonth month;

    // ✅ Getters & Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getDay() {
        return day;
    }

    public void setDay(Integer day) {
        this.day = day;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public RefMonth getMonth() {
        return month;
    }

    public void setMonth(RefMonth month) {
        this.month = month;
    }

}