package com.example.Business.mappers.impl;

import com.example.Business.domain.dto.DepartmentDto;
import com.example.Business.domain.entities.Department;
import com.example.Business.mappers.Mapper;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class DepartmentMapper implements Mapper<Department, DepartmentDto> {

    private ModelMapper modelMapper;

    public DepartmentMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    @Override
    public DepartmentDto mapTo(Department department) {
        return modelMapper.map(department, DepartmentDto.class);
    }

    @Override
    public Department mapFrom(DepartmentDto departmentDto) {
        return modelMapper.map(departmentDto, Department.class);
    }
}
