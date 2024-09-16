package com.example.Business.repositories;


import com.example.Business.TestDataUtil;
import com.example.Business.domain.entities.Department;
import com.example.Business.domain.entities.Employee;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class DepartmentRepositoryIntegrationTests {

    private DepartmentRepository underTest;

    @Autowired
    public DepartmentRepositoryIntegrationTests(DepartmentRepository underTest) {
        this.underTest = underTest;
    }

    @Test
    public void testThatDepartmentCanBeCreatedAndRecalled() {
        Department departmentEntity = TestDataUtil.createDepartmentIT();
        Department savedDepartmentEntity1 = underTest.save(departmentEntity);

        Optional<Department> result = underTest.findById(departmentEntity.getId());
        assertThat(result).isPresent();
        assertThat(result.get()).isEqualTo(departmentEntity);

        departmentEntity = TestDataUtil.createDepartmentIT();
        Department savedDepartmentEntity2 = underTest.save(departmentEntity);

        result = underTest.findById(departmentEntity.getId());
        assertThat(result).isPresent();
        assertThat(result.get()).isEqualTo(departmentEntity);

        assertThat(savedDepartmentEntity1).isEqualTo(savedDepartmentEntity2);

    }

    @Test
    public void testThatDepartmentCanBeUpdated() {
        Department departmentEntity = TestDataUtil.createDepartmentIT();
        underTest.save(departmentEntity);
        departmentEntity.setMaxSalary(9000.0);
        underTest.save(departmentEntity);
        Optional<Department> result = underTest.findById(departmentEntity.getId());
        assertThat(result).isPresent();
        assertThat(result.get().getMaxSalary()).isEqualTo(9000.0);
    }

    @Test
    public void testThatDepartmentCanBeDeleted() {

        Department departmentEntity = TestDataUtil.createDepartmentHR();
        underTest.save(departmentEntity);
        underTest.delete(departmentEntity);

        Optional<Department> result = underTest.findById(departmentEntity.getId());
        assertThat(result).isNotPresent();
    }
}
