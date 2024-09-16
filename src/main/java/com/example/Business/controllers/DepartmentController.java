package com.example.Business.controllers;

import com.example.Business.domain.dto.DepartmentDto;
import com.example.Business.domain.entities.Department;
import com.example.Business.mappers.Mapper;
import com.example.Business.mappers.impl.DepartmentMapper;
import com.example.Business.services.DepartmentService;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
public class DepartmentController {

    private final DepartmentService departmentService;
    private Mapper<Department, DepartmentDto> departmentMapper;

    public DepartmentController(DepartmentService departmentService, Mapper<Department, DepartmentDto> departmentMapper) {
        this.departmentService = departmentService;
        this.departmentMapper = departmentMapper;
    }

    @PostMapping(path = "/department")
    public ResponseEntity<Object> addDepartment(@RequestBody DepartmentDto departmentDto) {

        Department department = departmentMapper.mapFrom(departmentDto);

        if(departmentService.isExists(department.getName())) {
            String message = "The department with name " + department.getName() + " already exists.";
            return new ResponseEntity<>(message, HttpStatus.CONFLICT);
        }

        if(departmentService.isExists(department.getId())) {
            String message = "The department with id " + department.getId() + " already exists.";
            return new ResponseEntity<>(message, HttpStatus.CONFLICT);
        }

        Department savedDepartment = departmentService.save(department);
        return new ResponseEntity<>(departmentMapper.mapTo(savedDepartment), HttpStatus.CREATED);
    }

    @GetMapping(path = "/department")
    public List<DepartmentDto> listDepartments() {
        List<Department> departments = departmentService.findAllDepartments();
        return departments.stream()
                .map(departmentMapper::mapTo)
                .collect(Collectors.toList());
    }

    @GetMapping(path = "/department/{id}")
    public ResponseEntity<Object> getDepartment(@PathVariable("id") Long id) {
        Optional<Department> foundDepartment = departmentService.findOne(id);

        if(foundDepartment.isPresent()) {
            DepartmentDto departmentDto = departmentMapper.mapTo(foundDepartment.get());
            return new ResponseEntity<>(departmentDto, HttpStatus.OK);
        }
        else {
            String message = "The department with id " + id + " does not exist.";
            return new ResponseEntity<>(message, HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping(path = "/department/{id}")
    public ResponseEntity<Object> fullUpdateDepartment(@PathVariable("id") Long id, @RequestBody DepartmentDto departmentDto) {

        if(!departmentService.isExists(id)) {
            String message = "The department with id " + id + " does not exist.";
            return new ResponseEntity<>(message, HttpStatus.NOT_FOUND);
        }

        departmentDto.setId(id);
        Department department = departmentMapper.mapFrom(departmentDto);
        Department savedDepartment = departmentService.save(department);
        return new ResponseEntity<>(
                departmentMapper.mapTo(savedDepartment),
                HttpStatus.OK);
    }

    @DeleteMapping(path = "/department/{id}")
    public ResponseEntity deleteDepartment(@PathVariable("id") Long id) {

        if(!departmentService.isExists(id)) {
            String message = "The department with id " + id + " does not exist.";
            return new ResponseEntity<>(message, HttpStatus.NOT_FOUND);
        }

        boolean success = departmentService.delete(id);

        if(success) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        else {
            String message = "The department with id " + id + " does not exist.";
            return new ResponseEntity<>(message, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PatchMapping("/department/{id}/max-salary")
    public ResponseEntity<Object> increaseMaxSalary(@PathVariable Long id, @RequestParam Double increase) {

        if (!departmentService.isExists(id)) {
            String message = "The department with id " + id + " does not exist.";
            return new ResponseEntity<>(message, HttpStatus.NOT_FOUND);
        }

        Department updatedDepartment = departmentService.increaseMaxSalary(id, increase);
        DepartmentDto departmentDto = departmentMapper.mapTo(updatedDepartment);
        return new ResponseEntity<>(departmentDto, HttpStatus.OK);
    }
}
