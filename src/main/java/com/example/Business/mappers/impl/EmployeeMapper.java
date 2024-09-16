package com.example.Business.mappers.impl;

import com.example.Business.domain.dto.EmployeeDto;
import com.example.Business.domain.entities.Employee;
import com.example.Business.mappers.Mapper;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class EmployeeMapper implements Mapper<Employee, EmployeeDto> {

    private ModelMapper modelMapper;

    public EmployeeMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    @Override
    public EmployeeDto mapTo(Employee employee) {
        return modelMapper.map(employee, EmployeeDto.class);
    }

    @Override
    public Employee mapFrom(EmployeeDto employeeDto) {
        return modelMapper.map(employeeDto, Employee.class);
    }
}
