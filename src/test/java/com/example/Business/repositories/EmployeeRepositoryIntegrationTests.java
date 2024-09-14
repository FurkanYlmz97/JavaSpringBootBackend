package com.example.Business.repositories;

import com.example.Business.domain.entities.Employee;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import com.example.Business.TestDataUtil;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;


@SpringBootTest
@ExtendWith(SpringExtension.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class EmployeeRepositoryIntegrationTests {

    private EmployeeRepository underTest;

    @Autowired
    public EmployeeRepositoryIntegrationTests(EmployeeRepository underTest) {
        this.underTest = underTest;
    }

    @Test
    public void testThatManagerCanBeCreatedAndRecalled() {
        Employee managerEntity = TestDataUtil.createManager();
        underTest.save(managerEntity);
        Optional<Employee> result = underTest.findById(managerEntity.getId());
        assertThat(result).isPresent();
        assertThat(result.get()).isEqualTo(managerEntity);
    }

    @Test
    public void testThatEmployeeCanBeCreatedAndRecalled() {
        Employee managerEntity = TestDataUtil.createManager();
        underTest.save(managerEntity);

        Employee employeeEntity = TestDataUtil.createEmployeeEntityA(managerEntity, managerEntity.getDepartment());
        underTest.save(employeeEntity);

        Optional<Employee> result = underTest.findById(employeeEntity.getId());
        assertThat(result).isPresent();
        assertThat(result.get()).isEqualTo(employeeEntity);
        assertThat(result.get()).isNotEqualTo(managerEntity);
        assertThat(result.get().getDepartment()).isEqualTo(managerEntity.getDepartment());
        assertThat(result.get().getManager()).isEqualTo(managerEntity);
    }

    @Test
    public void testThatManagerCanBeUpdated() {
        Employee managerEntity = TestDataUtil.createManager();
        underTest.save(managerEntity);
        managerEntity.setSalary(5000.0);
        underTest.save(managerEntity);
        Optional<Employee> result = underTest.findById(managerEntity.getId());
        assertThat(result).isPresent();
        assertThat(result.get().getSalary()).isEqualTo(5000.0);
    }

    @Test
    public void testThatManagerCanBeDeleted() {

        Employee managerEntity = TestDataUtil.createManager();
        managerEntity.setDepartment(null);
        underTest.save(managerEntity);
        underTest.delete(managerEntity);

        Optional<Employee> result = underTest.findById(managerEntity.getId());
        assertThat(result).isNotPresent();
    }

}
