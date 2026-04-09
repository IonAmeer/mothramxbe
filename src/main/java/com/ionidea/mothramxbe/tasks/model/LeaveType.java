package com.ionidea.mothramxbe.tasks.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "leave_types")
public class LeaveType {

    @Id
    private Integer id;

    private String name;

    private String description;

}
