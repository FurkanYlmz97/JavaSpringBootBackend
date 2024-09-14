package com.example.Business.domain.dto;

import com.example.Business.domain.entities.Department;
import com.example.Business.domain.entities.Employee;
//import com.example.Business.domain.entities.Project;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeDto {

    private long id;
    private String name;
    private String email;
    private String position;
    private Integer yearsOfExperience;
    private Department department;
    private Employee manager;
}
