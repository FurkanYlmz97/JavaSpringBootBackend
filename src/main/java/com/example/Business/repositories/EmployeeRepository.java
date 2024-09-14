package com.example.Business.repositories;

import com.example.Business.domain.entities.Department;
import com.example.Business.domain.entities.Employee;
import org.apache.catalina.Manager;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EmployeeRepository extends CrudRepository<Employee, Long> {

    List<Employee> findAllByDepartment(Department department);

    List<Employee> findAllByManager(Employee manager);

}
