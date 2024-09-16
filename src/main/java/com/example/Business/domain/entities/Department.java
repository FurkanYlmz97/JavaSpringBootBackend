package com.example.Business.domain.entities;

import java.util.List;
import java.util.Objects;

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
@Table(name = "departments")
public class Department {

    @Id
    private Long id;
    private String name;
    private Double maxSalary;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Department department = (Department) o;
        return Objects.equals(name, department.name) &&
                Objects.equals(id, department.id) &&
                Objects.equals(maxSalary, department.maxSalary);
    }

    @Override
    public int hashCode() {
        // Combine hash codes of the fields used in equals
        return Objects.hash(name, id, maxSalary);
    }
}
