package com.example.Business.domain.dto;

import com.example.Business.domain.entities.Employee;
//import com.example.Business.domain.entities.Project;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DepartmentDto {

    private Long id;
    private String name;
    private Double maxSalary;
}
