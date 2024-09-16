package com.example.Business.services.impl;

import com.example.Business.domain.entities.Department;
import com.example.Business.domain.entities.Employee;
import com.example.Business.repositories.DepartmentRepository;
import com.example.Business.repositories.EmployeeRepository;
import com.example.Business.services.EmployeeService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final DepartmentRepository departmentRepository;

    @PersistenceContext
    private EntityManager entityManager;

    public EmployeeServiceImpl(EmployeeRepository employeeRepository, EntityManager entityManager, DepartmentRepository departmentRepository) {
        this.employeeRepository = employeeRepository;
        this.entityManager = entityManager;
        this.departmentRepository = departmentRepository;
    }

    @Override
    public boolean isExists(Long id) {
        return employeeRepository.existsById(id);
    }

    @Override
    public boolean isExists(String email) {
        return employeeRepository.existsByEmail(email);
    }

    @Override
    @Transactional
    public Employee save(Employee employee) {

        Department checkDepartment = employee.getDepartment();
        if (checkDepartment!= null) {
            Optional<Department> department = departmentRepository.findById(checkDepartment.getId());
            if (department.isEmpty()) {
                departmentRepository.save(checkDepartment);
            }
        }

        Employee checkManager = employee.getManager();
        if (checkManager != null) {
            Optional<Employee> manager = employeeRepository.findById(checkManager.getId());
            if (manager.isEmpty()) {
                employeeRepository.save(checkManager);
            }
        }

        return employeeRepository.save(employee);
    }

    @Override
    public List<Employee> findAllByDepartmentId(Long departmentId) {
        return StreamSupport.stream(employeeRepository
                                .findAllByDepartmentId(departmentId)
                                .spliterator(),
                        false)
                .collect(Collectors.toList());
    }

    @Override
    public List<Employee> findAll() {
        return StreamSupport.stream(employeeRepository
                                .findAll()
                                .spliterator(),
                        false)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<Employee> findOne(Long id) {
        return employeeRepository.findById(id);
    }


    @Override
    @Transactional
    public boolean delete(Long id) {

        Optional<Employee> retrieved = employeeRepository.findById(id);

        if (retrieved.isPresent()) {

            List<Employee> workers = StreamSupport.stream(employeeRepository.findAllByManager(retrieved.get())
                                    .spliterator(),
                            false)
                    .collect(Collectors.toList());

            for (Employee employee : workers) {
                employee.setManager(null);
                employeeRepository.save(employee);
            }

            retrieved.get().setDepartment(null);
            employeeRepository.save(retrieved.get());
            employeeRepository.deleteById(id);
            return true;
        }
        return false;
    }

    @Override
    public Employee promote(Long id, Double promotion) {

        Optional<Employee> result = employeeRepository.findById(id);

        if (result.isPresent()) {
            Employee employee = result.get();
            if ((employee.getSalary() + promotion) <= employee.getDepartment().getMaxSalary() &&
            employee.getYearsOfExperience() >= 2) {
                employee.setSalary(employee.getSalary() + promotion);
                employeeRepository.save(employee);
                return employee;
            }
            return null;
        }
        else {
            return null;
        }
    }

    @Override
    public Employee addExperience(Long id, Integer year) {

        Optional<Employee> result = employeeRepository.findById(id);
        Employee employee = result.get();
        employee.setYearsOfExperience(employee.getYearsOfExperience() + year);
        return employeeRepository.save(employee);
    }

    @Override
    public Employee assignToDepartment(Long id, Long departmentId) {
        Optional<Employee> result = employeeRepository.findById(id);
        Optional<Department> departmentResult = departmentRepository.findById(departmentId);
        Employee employee = result.get();
        Department department = departmentResult.get();

        if (employee.getSalary() <= department.getMaxSalary()){
            employee.setDepartment(department);
            return employeeRepository.save(employee);
        }
        return null;
    }
}
