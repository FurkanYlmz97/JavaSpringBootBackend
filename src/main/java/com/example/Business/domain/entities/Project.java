package com.example.Business.domain.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "projects")
public class Project {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private Double budget;

    @ManyToOne
    @JoinColumn(name = "department_id")
    private Department epartment;

    @ManyToMany(mappedBy = "projects")
    private List<Employee> employees;


}
