package com.example.Business.services;

import com.example.Business.TestDataUtil;
import com.example.Business.domain.entities.Department;
import com.example.Business.domain.entities.Employee;
import com.example.Business.repositories.EmployeeRepository;
import com.example.Business.services.impl.EmployeeServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


@SpringBootTest
@ExtendWith(SpringExtension.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class EmployeeServiceIntegrationTest {

    private final EmployeeService undertest;

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

        Employee employee = TestDataUtil.createEmployeeEntityA(savedManager, savedManager.getDepartment());
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

        savedManager.setDepartment(hrDepartment);
        undertest.save(savedManager);
        retrievedManager = undertest.findOne(savedManager.getId());

        assertNotEquals(department, retrievedManager.get().getDepartment());
        assertEquals(hrDepartment, retrievedManager.get().getDepartment());
    }
}
