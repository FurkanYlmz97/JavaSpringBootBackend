package com.example.Business.repositories;

import com.example.Business.domain.entities.Department;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DepartmentRepository extends CrudRepository<Department, Long> {

    boolean existsByName(String name);
}
