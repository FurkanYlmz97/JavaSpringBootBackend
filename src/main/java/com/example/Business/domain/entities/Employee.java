package com.example.Business.domain.entities;

import java.util.List;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "employees")
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String name;
    private String email;
    private String position;
    private Double salary;
    private Integer yearsOfExperience;
    private Integer performanceRating;

    @ManyToOne
    @JoinColumn(name = "department_id")
    private Department department;

    @ManyToOne
    @JoinColumn(name = "employee_id")
    private Employee manager;

    @ManyToMany
    @JoinTable(
            name = "employee_projects", // Join table name
            joinColumns = @JoinColumn(name = "employee_id"), // Column for the employee
            inverseJoinColumns = @JoinColumn(name = "project_id") // Column for the project
    )
    private List<Project> projects;


}
