package com.example.Business.services;

import com.example.Business.domain.entities.Department;
import com.example.Business.domain.entities.Employee;

import java.util.List;
import java.util.Optional;

public interface EmployeeService {

    Employee save(Employee employee);

    List<Employee> findAllByDepartment(Department department);

    List<Employee> findAll();

    Optional<Employee> findOne(Long id);

    boolean isExists(Long id);

    boolean delete(Long id);

    Employee promote(Long id, Double promotion);

    void assignToDepartment(Long id, Department department);
}
