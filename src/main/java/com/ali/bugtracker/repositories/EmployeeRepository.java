package com.ali.bugtracker.repositories;

import ch.qos.logback.core.boolex.EvaluationException;
import com.ali.bugtracker.entities.Employee;
import com.ali.bugtracker.entities.Project;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import javax.validation.constraints.Max;
import java.math.BigInteger;
import java.util.List;


public interface EmployeeRepository extends CrudRepository<Employee,Long> {

    public List<Employee> findAll();
    public Employee findByEmail(String email);
    public Employee findByEmployeeId(Long id);

    @Query(value="select employee_id from employee_project e where e.project_Id=:projectId ", nativeQuery=true)
    public List<Long> findAllByProjectId(@Param("projectId") Long projectId);

    @Query(value="select * from employee e where e.role=:theRole",
            nativeQuery=true)
    public List<Employee> findAllByRoles(String theRole);
}
