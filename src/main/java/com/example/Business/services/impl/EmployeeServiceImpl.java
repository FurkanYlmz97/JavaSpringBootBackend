package com.example.Business.services.impl;

import com.example.Business.domain.entities.Department;
import com.example.Business.domain.entities.Employee;
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

    @PersistenceContext
    private EntityManager entityManager;

    public EmployeeServiceImpl(EmployeeRepository employeeRepository, EntityManager entityManager) {
        this.employeeRepository = employeeRepository;
        this.entityManager = entityManager;
    }

    @Override
    public boolean isExists(Long id) {
        return employeeRepository.existsById(id);
    }

    @Override
    public Employee save(Employee employee) {
        return employeeRepository.save(employee);
    }

    @Override
    public List<Employee> findAllByDepartment(Department department) {
        return StreamSupport.stream(employeeRepository
                                .findAllByDepartment(department)
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
            }
            return employee;
        }
        else {
            return null;
        }
    }

    @Override
    public void assignToDepartment(Long id, Department department) {
        Optional<Employee> result = employeeRepository.findById(id);

        if (result.isPresent()) {
            Employee employee = result.get();
            employee.setDepartment(department);
            employeeRepository.save(employee);
        }
    }
}
