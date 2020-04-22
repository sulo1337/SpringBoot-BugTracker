package com.ali.bugtracker.repositories;

import com.ali.bugtracker.entities.Project;
import org.springframework.data.repository.CrudRepository;

public interface ProjectRepository extends CrudRepository<Project,Long> {
}
