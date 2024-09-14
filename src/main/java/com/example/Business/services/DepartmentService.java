package com.example.Business.services;

import com.example.Business.domain.entities.Department;
import com.example.Business.domain.entities.Employee;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

public interface DepartmentService {

    Department save(Department department);

    List<Employee> findAllEmployees(Long id);

    List<Department> findAllDepartments();

    Optional<Department> findOne(Long id);

    boolean isExists(Long id);

    boolean delete(Long id);

    Department increaseMaxSalary(Long id, Double increase);


}
