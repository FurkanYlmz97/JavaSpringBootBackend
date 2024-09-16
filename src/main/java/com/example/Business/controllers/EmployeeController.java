package com.example.Business.controllers;

import com.example.Business.domain.dto.DepartmentDto;
import com.example.Business.domain.dto.EmployeeDto;
import com.example.Business.domain.entities.Department;
import com.example.Business.domain.entities.Employee;
import com.example.Business.mappers.Mapper;
import com.example.Business.services.DepartmentService;
import com.example.Business.services.EmployeeService;
import jakarta.websocket.server.PathParam;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
public class EmployeeController {

    private final EmployeeService employeeService;
    private final DepartmentService departmentService;
    private final Mapper<Employee, EmployeeDto> employeeMapper;

    public EmployeeController(EmployeeService employeeService, DepartmentService departmentService, Mapper<Employee, EmployeeDto> employeeMapper) {
        this.employeeService = employeeService;
        this.employeeMapper = employeeMapper;
        this.departmentService = departmentService;
    }

    @PostMapping(path = "/employee")
    public ResponseEntity<Object> addEmployee(@RequestBody EmployeeDto employeeDto) {

        Employee employee = employeeMapper.mapFrom(employeeDto);

        if(employeeService.isExists(employee.getEmail())) {
            String message = "The employee with email " + employee.getEmail() + " already exists.";
            return new ResponseEntity<>(message, HttpStatus.CONFLICT);
        }

        Employee manager = employee.getManager();

        if (manager != null) {
            if (manager.equals(employee)) {
                String message = "Employee cannot be its manager";
                return new ResponseEntity<>(message, HttpStatus.BAD_REQUEST);
            }
        }

        Employee savedEmployee = employeeService.save(employee);
        return new ResponseEntity<>(employeeMapper.mapTo(savedEmployee), HttpStatus.CREATED);
    }

    @GetMapping(path = "/employee")
    public List<EmployeeDto> listEmployees() {
        List<Employee> employees = employeeService.findAll();
        return employees.stream()
                .map(employeeMapper::mapTo)
                .collect(Collectors.toList());
    }

    @GetMapping(path = "/employee/{id}")
    public ResponseEntity<Object> getEmployee(@PathVariable("id") Long id) {
        Optional<Employee> foundEmployee = employeeService.findOne(id);

        if(foundEmployee.isPresent()) {
            EmployeeDto employeeDto = employeeMapper.mapTo(foundEmployee.get());
            return new ResponseEntity<>(employeeDto, HttpStatus.OK);
        }
        else {
            String message = "The employee with id " + id + " does not exist.";
            return new ResponseEntity<>(message, HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping(path = "/employee/by-department")
    public List<EmployeeDto> getEmployeesByDepartment(@RequestParam Long departmentId) {

        List<Employee> foundEmployees = employeeService.findAllByDepartmentId(departmentId);

        return foundEmployees.stream()
                .map(employeeMapper::mapTo)
                .collect(Collectors.toList());
    }

    @PutMapping(path = "/employee/{id}")
    public ResponseEntity<Object> fullUpdateEmployee(@PathVariable("id") Long id, @RequestBody EmployeeDto employeeDto) {

        if(!employeeService.isExists(id)) {
            String message = "The employee with id " + id + " does not exist.";
            return new ResponseEntity<>(message, HttpStatus.NOT_FOUND);
        }

        employeeDto.setId(id);
        Employee employee = employeeMapper.mapFrom(employeeDto);
        Employee savedEmployee = employeeService.save(employee);
        return new ResponseEntity<>(
                employeeMapper.mapTo(savedEmployee),
                HttpStatus.OK);
    }

    @DeleteMapping(path = "/employee/{id}")
    public ResponseEntity deleteEmployee(@PathVariable("id") Long id) {

        if(!employeeService.isExists(id)) {
            String message = "The employee with id " + id + " does not exist.";
            return new ResponseEntity<>(message, HttpStatus.NOT_FOUND);
        }

        boolean success = employeeService.delete(id);

        if(success) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        else {
            String message = "The employee with id " + id + " does not exist.";
            return new ResponseEntity<>(message, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PatchMapping("/employee/{id}/promote")
    public ResponseEntity<Object> increaseSalary(@PathVariable Long id, @RequestParam Double increase) {

        if (!employeeService.isExists(id)) {
            String message = "The employee with id " + id + " does not exist.";
            return new ResponseEntity<>(message, HttpStatus.NOT_FOUND);
        }

        Employee updatedEmployee = employeeService.promote(id, increase);
        if(updatedEmployee == null) {
            String message = "The employee with id " + id + " cannot be promoted.";
            return new ResponseEntity<>(message, HttpStatus.BAD_REQUEST);
        }

        EmployeeDto employeeDto = employeeMapper.mapTo(updatedEmployee);
        return new ResponseEntity<>(employeeDto, HttpStatus.OK);
    }

    @PatchMapping("/employee/{employeeId}/assign")
    public ResponseEntity<Object> assignDepartment(@PathVariable Long employeeId, @RequestParam Long departmentId) {

        if (!employeeService.isExists(employeeId)) {
            String message = "The employee with id " + employeeId + " does not exist.";
            return new ResponseEntity<>(message, HttpStatus.NOT_FOUND);
        }

        if (!departmentService.isExists(departmentId)) {
            String message = "The department with id " + departmentId + " does not exist.";
            return new ResponseEntity<>(message, HttpStatus.NOT_FOUND);
        }

        Employee updatedEmployee = employeeService.assignToDepartment(employeeId, departmentId);

        if(updatedEmployee == null) {
            String message = "Employees current salary higher than the departments maximum salary that wanted to be changed!";
            return new ResponseEntity<>(message, HttpStatus.BAD_REQUEST);
        }

        EmployeeDto employeeDto = employeeMapper.mapTo(updatedEmployee);
        return new ResponseEntity<>(employeeDto, HttpStatus.OK);
    }


    @PatchMapping("/employee/{employeeId}/experience")
    public ResponseEntity<Object> increaseExperience(@PathVariable Long employeeId, @RequestParam Integer experienceYearToAdd) {

        if (!employeeService.isExists(employeeId)) {
            String message = "The employee with id " + employeeId + " does not exist.";
            return new ResponseEntity<>(message, HttpStatus.NOT_FOUND);
        }

        Employee updatedEmployee = employeeService.addExperience(employeeId, experienceYearToAdd);
        EmployeeDto employeeDto = employeeMapper.mapTo(updatedEmployee);
        return new ResponseEntity<>(employeeDto, HttpStatus.OK);
    }
}
