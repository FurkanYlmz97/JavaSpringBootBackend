package com.example.Business.services.impl;

import com.example.Business.domain.entities.Department;
import com.example.Business.domain.entities.Employee;
import com.example.Business.repositories.DepartmentRepository;
import com.example.Business.repositories.EmployeeRepository;
import com.example.Business.services.DepartmentService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class DepartmentServiceImpl implements DepartmentService {

    private DepartmentRepository departmentRepository;
    private EmployeeRepository employeeRepository;

    public DepartmentServiceImpl(DepartmentRepository departmentRepository, EmployeeRepository employeeRepository) {
        this.departmentRepository = departmentRepository;
        this.employeeRepository = employeeRepository;
    }

    @Override
    public Department save(Department department) {
        return departmentRepository.save(department);
    }

    @Override
    public List<Employee> findAllEmployees(Long id) {

        Department department = departmentRepository.findById(id).orElse(null);

        if (department != null) {
            return StreamSupport.stream(employeeRepository
                                    .findAllByDepartmentId(id)
                                    .spliterator(),
                            false)
                    .collect(Collectors.toList());
        }
        return null;
    }

    @Override
    public List<Department> findAllDepartments() {

        return StreamSupport.stream(departmentRepository
                                .findAll()
                                .spliterator(),
                        false)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<Department> findOne(Long id) {
        return departmentRepository.findById(id);
    }

    @Override
    public boolean isExists(String name) {
        return departmentRepository.existsByName(name);
    }

    @Override
    public boolean isExists(Long id) {
        return departmentRepository.existsById(id);
    }

    @Override
    @Transactional
    public boolean delete(Long id) {

        Optional<Department> department = departmentRepository.findById(id);

        if (department.isPresent()) {
            List<Employee> workers = findAllEmployees(id);

            for (Employee worker : workers) {
                worker.setDepartment(null);
                employeeRepository.save(worker);
            }

            departmentRepository.deleteById(id);
            return true;
        }
        return false;
    }

    @Override
    @Transactional
    public Department increaseMaxSalary(Long id, Double increase) {

        Optional<Department> department = departmentRepository.findById(id);

        if (department.isPresent()) {
            department.get().setMaxSalary(department.get().getMaxSalary() + increase);
            departmentRepository.save(department.get());
            return department.get();
        }

        return null;
    }
}
