package com.ali.bugtracker.repositories;

import ch.qos.logback.core.boolex.EvaluationException;
import com.ali.bugtracker.dto.EmployeeProject;
import com.ali.bugtracker.entities.Employee;
import com.ali.bugtracker.entities.Project;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import javax.validation.constraints.Max;
import java.math.BigInteger;
import java.util.List;


public interface EmployeeRepository extends CrudRepository<Employee,Long> {

     List<Employee> findAll();
     Employee findByEmail(String email);
     Employee findByEmployeeId(Long id);

    @Query(value="select employee_id from employee_project e where e.project_Id=:projectId ", nativeQuery=true)
     List<Long> findAllByProjectId(@Param("projectId") Long projectId);

    @Query(value="select * from employee e where e.role=:theRole",
            nativeQuery=true)
     List<Employee> findAllByRoles(String theRole);

    @Query(nativeQuery = true,value ="select e.first_name as firstName, e.last_name as lastName ,Count(pe.employee_id) as projectCount " +
            "from employee e left join employee_project pe ON pe.employee_id=e.employee_id " +
            "group by e.first_name,e.last_name order by 3 desc")
     List<EmployeeProject> employeeProjects();
}
