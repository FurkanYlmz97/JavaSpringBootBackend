package com.example.Business.services;


import com.example.Business.TestDataUtil;
import com.example.Business.domain.entities.Department;
import com.example.Business.domain.entities.Employee;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class DepartmentServiceIntegrationTest {

    private DepartmentService undertest;
    private EmployeeService employeeService;

    @Autowired
    public DepartmentServiceIntegrationTest(DepartmentService undertest, EmployeeService employeeService) {
        this.undertest = undertest;
        this.employeeService = employeeService;
    }


    @Test
    void testThatDepartmentSaveAndReturn() {
        Department department = TestDataUtil.createDepartmentIT();
        Department savedDepartment = undertest.save(department);
        Optional<Department> retrievedDepartment = undertest.findOne(savedDepartment.getId());

        assertTrue(retrievedDepartment.isPresent());
        assertEquals(savedDepartment, retrievedDepartment.get());
    }

    @Test
    void testThatDepartmentDelete() {

        Employee manager = TestDataUtil.createManager();
        Employee savedManager = employeeService.save(manager);

        Employee employee = TestDataUtil.createEmployeeEntityA(savedManager, savedManager.getDepartment());
        Employee savedEmployee = employeeService.save(manager);

        assertTrue(undertest.delete(savedEmployee.getDepartment().getId()));

        Optional<Employee> retrievedManager = employeeService.findOne(savedManager.getId());
        Optional<Employee> retrievedEmployee = employeeService.findOne(savedEmployee.getId());

        assertTrue(retrievedManager.isPresent());
        assertTrue(retrievedEmployee.isPresent());

        assertNull(retrievedEmployee.get().getDepartment());
        assertNull(retrievedManager.get().getDepartment());
    }

    @Test
    void testIncreaseMaxSalary() {

        Department department = TestDataUtil.createDepartmentIT();
        Department savedDepartment = undertest.save(department);
        Optional<Department> retrievedDepartment = undertest.findOne(department.getId());

        assertTrue(retrievedDepartment.isPresent());
        assertEquals(department.getMaxSalary(), retrievedDepartment.get().getMaxSalary());

        savedDepartment.setMaxSalary(savedDepartment.getMaxSalary() + 1000);
        undertest.save(savedDepartment);

        retrievedDepartment = undertest.findOne(savedDepartment.getId());
        assertEquals(department.getMaxSalary() + 1000, retrievedDepartment.get().getMaxSalary());
    }
}
