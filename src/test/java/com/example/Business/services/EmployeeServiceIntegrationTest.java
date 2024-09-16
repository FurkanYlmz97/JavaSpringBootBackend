package com.example.Business.services;

import com.example.Business.TestDataUtil;
import com.example.Business.domain.entities.Department;
import com.example.Business.domain.entities.Employee;
import com.example.Business.repositories.DepartmentRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;


@SpringBootTest
@ExtendWith(SpringExtension.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class EmployeeServiceIntegrationTest {

    private final EmployeeService undertest;
    @Autowired
    private DepartmentRepository departmentRepository;

    @Autowired
    public EmployeeServiceIntegrationTest(EmployeeService undertest) {
        this.undertest = undertest;
    }

    @Test
    void testThatEmployeeSaveAndReturn() {
        Employee employee = TestDataUtil.createManager();
        Employee savedEmployee = undertest.save(employee);
        Optional<Employee> retrievedEmployee = undertest.findOne(savedEmployee.getId());

        assertTrue(retrievedEmployee.isPresent());
        assertEquals(savedEmployee, retrievedEmployee.get());
    }

    @Test
    void testThatManagerAndEmployeeSaveAndReturn() {
        Employee manager = TestDataUtil.createManager();
        Employee savedManager = undertest.save(manager);
        Optional<Employee> retrievedManager = undertest.findOne(savedManager.getId());

        Employee employee = TestDataUtil.createEmployeeClara(savedManager, retrievedManager.get().getDepartment());
        Employee savedEmployee = undertest.save(employee);
        Optional<Employee> retrievedEmployee = undertest.findOne(savedEmployee.getId());

        assertTrue(retrievedManager.isPresent());
        assertEquals(savedManager, retrievedManager.get());

        assertTrue(retrievedEmployee.isPresent());
        assertEquals(savedEmployee, retrievedEmployee.get());
    }

    @Test
    void testThatEmployeeDelete() {
        Employee employee = TestDataUtil.createManager();
        Employee savedEmployee = undertest.save(employee);
        Optional<Employee> retrievedEmployee = undertest.findOne(savedEmployee.getId());
        assertTrue(retrievedEmployee.isPresent());

        undertest.delete(employee.getId());
        retrievedEmployee = undertest.findOne(employee.getId());
        assertTrue(retrievedEmployee.isEmpty());
    }

    @Test
    void testThatManagerDeletedEmployeeStays() {
        Employee manager = TestDataUtil.createManager();
        Employee savedManager = undertest.save(manager);
        Optional<Employee> retrievedManager = undertest.findOne(savedManager.getId());

        Employee employee = TestDataUtil.createEmployeeClara(savedManager, savedManager.getDepartment());
        Employee savedEmployee = undertest.save(employee);
        Optional<Employee> retrievedEmployee = undertest.findOne(savedEmployee.getId());

        assertTrue(retrievedManager.isPresent());
        assertTrue(retrievedEmployee.isPresent());

        assertTrue(undertest.delete(savedManager.getId()));

        retrievedManager = undertest.findOne(savedManager.getId());
        retrievedEmployee = undertest.findOne(savedEmployee.getId());

        assertTrue(retrievedManager.isEmpty());
        assertTrue(retrievedEmployee.isPresent());
        assertNull(retrievedEmployee.get().getManager());
    }

    @Test
    void testPromotion() {
        Employee manager = TestDataUtil.createManager();
        Employee savedManager = undertest.save(manager);
        Optional<Employee> retrievedManager = undertest.findOne(savedManager.getId());

        Double oldSalary = savedManager.getSalary();
        undertest.promote(savedManager.getId(), 300.0);

        assertEquals(oldSalary + 300.0, undertest.findOne(savedManager.getId()).get().getSalary());

        oldSalary = oldSalary + 300.0;
        undertest.promote(savedManager.getId(), 3000.0);
        assertNotEquals(oldSalary + 3000.0, undertest.findOne(savedManager.getId()).get().getSalary());

        savedManager.setYearsOfExperience(1);
        undertest.save(savedManager);
        undertest.promote(savedManager.getId(), 300.0);
        assertNotEquals(oldSalary + 300.0, undertest.findOne(savedManager.getId()).get().getSalary());
    }

    @Test
    void testAssignDepartment() {
        Employee manager = TestDataUtil.createManager();
        Employee savedManager = undertest.save(manager);
        Optional<Employee> retrievedManager = undertest.findOne(savedManager.getId());

        Department department = manager.getDepartment();
        Department hrDepartment = TestDataUtil.createDepartmentHR();

        assertEquals(department, retrievedManager.get().getDepartment());

        Department savedDepartment = departmentRepository.save(hrDepartment);
        undertest.assignToDepartment(savedManager.getId(), savedDepartment.getId());

        retrievedManager = undertest.findOne(savedManager.getId());

        assertNotEquals(department, retrievedManager.get().getDepartment());
        assertEquals(hrDepartment, retrievedManager.get().getDepartment());
    }

    @Test
    void testAddExperience() {
        Employee manager = TestDataUtil.createManager();
        Employee savedManager = undertest.save(manager);
        Optional<Employee> retrievedManager = undertest.findOne(savedManager.getId());

        savedManager = undertest.addExperience(savedManager.getId(), 1);

        assertEquals(savedManager.getYearsOfExperience(), retrievedManager.get().getYearsOfExperience() + 1);
    }
}
