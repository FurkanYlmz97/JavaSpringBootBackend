package com.example.Business.controllers;

import com.example.Business.domain.dto.DepartmentDto;
import com.example.Business.domain.entities.Department;
import com.example.Business.mappers.Mapper;
import com.example.Business.mappers.impl.DepartmentMapper;
import com.example.Business.services.DepartmentService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DepartmentController {

    private final DepartmentService departmentService;
    private Mapper<Department, DepartmentDto> departmentMapper;

    public DepartmentController(DepartmentService departmentService, Mapper<Department, DepartmentDto> departmentMapper) {
        this.departmentService = departmentService;
        this.departmentMapper = departmentMapper;
    }

    @PostMapping(path = "/department")
    public DepartmentDto addDepartment(@RequestBody DepartmentDto departmentDto) {
        Department department = departmentMapper.mapFrom(departmentDto);
        Department savedDepartment = departmentService.save(department);

        return departmentMapper.mapTo(savedDepartment);
    }
}
