package com.example.Business;

import com.example.Business.domain.entities.Department;
import com.example.Business.domain.entities.Employee;
import jakarta.persistence.OneToMany;

import java.util.List;

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

    public static Employee createEmployeeEntityA(Employee manager, Department department) {
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


    public static Employee createEmployeeEntityB(Employee manager, Department department) {
        return Employee.builder()
                .id(1L)
                .name("Clara")
                .email("clara@mail.com")
                .department(department)
                .manager(manager)
                .position("HR")
                .salary(3300.0)
                .yearsOfExperience(5)
                .performanceRating(5)
                .build();
    }

    public static Employee createEmployeeEntityC(Employee manager, Department department) {
        return Employee.builder()
                .id(1L)
                .name("Jacob")
                .email("kacob@mail.com")
                .department(department)
                .manager(manager)
                .position("HW")
                .salary(4300.0)
                .yearsOfExperience(5)
                .performanceRating(5)
                .build();
    }
}
