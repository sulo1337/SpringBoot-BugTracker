package com.ali.bugtracker.repositories;

import com.ali.bugtracker.entities.Employee;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;


public interface EmployeeRepository extends CrudRepository<Employee,Long> {

    public List<Employee> findAll();
    public Employee findByEmail(String email);

    @Query(value="select * from employee where role='ROLE_P' or role='ROLE_T' ",
            nativeQuery=true)
    public List<Employee> findAllByRoles();

}
