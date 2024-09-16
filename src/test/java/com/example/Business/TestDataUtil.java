package com.example.Business;

import com.example.Business.domain.entities.Department;
import com.example.Business.domain.entities.Employee;

public class TestDataUtil {

    private  TestDataUtil(){}

    public static Department createDepartmentIT() {
        return Department.builder()
                .id(1L)
                .name("IT")
                .maxSalary(5000.0)
                .build();
    }

    public static Department createDepartmentHR() {
        return Department.builder()
                .id(1L)
                .name("HR")
                .maxSalary(7000.0)
                .build();
    }

    public static Employee createManager() {
        return Employee.builder()
                .id(1L)
                .name("Michael")
                .email("michi@mail.com")
                .position("Software")
                .salary(3000.0)
                .yearsOfExperience(3)
                .performanceRating(4)
                .department(createDepartmentIT())
                .build();
    }

    public static Employee createEmployeeClara(Employee manager, Department department) {
        return Employee.builder()
                .id(2L)
                .name("Clara")
                .email("clara@mail.com")
                .department(department)
                .manager(manager)
                .position("Software")
                .salary(2000.0)
                .yearsOfExperience(1)
                .performanceRating(3)
                .build();
    }
}
