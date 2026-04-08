package com.ionidea.mothramxbe.entity;

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

    public Long getId() { return id; }

    public String getName() { return name; }

    public Integer getDay() { return day; }

    public Integer getYear() { return year; }

    public RefMonth getMonth() { return month; }

    public void setId(Long id) { this.id = id; }

    public void setName(String name) { this.name = name; }

    public void setDay(Integer day) { this.day = day; }

    public void setYear(Integer year) { this.year = year; }

    public void setMonth(RefMonth month) { this.month = month; }
}