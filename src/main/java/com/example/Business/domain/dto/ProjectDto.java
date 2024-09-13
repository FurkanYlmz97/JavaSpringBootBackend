package com.example.Business.domain.dto;

import com.example.Business.domain.entities.Department;
import com.example.Business.domain.entities.Employee;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProjectDto {

    private Long id;
    private String name;
    private Double budget;
    private Department epartment;
    private List<Employee> employees;
}
