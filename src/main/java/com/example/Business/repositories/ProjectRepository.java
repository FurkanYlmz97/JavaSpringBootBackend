package com.example.Business.repositories;

import com.example.Business.domain.entities.Project;
import org.springframework.data.repository.CrudRepository;

public interface ProjectRepository extends CrudRepository<Project, Long> {
}
