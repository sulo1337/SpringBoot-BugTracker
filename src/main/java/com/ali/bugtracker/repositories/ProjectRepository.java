package com.ali.bugtracker.repositories;


import com.ali.bugtracker.entities.Employee;
import com.ali.bugtracker.entities.Project;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProjectRepository extends CrudRepository<Project,Long> {
    public List<Project> findAll();
    public List<Project> findAllByOwner(Employee manager);
    public Project findByName(String name);
    public Project findByProjectId(Long id);
    @Query(value="select project_id from employee_project where employee_id=:employeeId",nativeQuery=true)
    public List<Long> findAllByEmployeeId(@Param("employeeId") Long employeeId);
}
