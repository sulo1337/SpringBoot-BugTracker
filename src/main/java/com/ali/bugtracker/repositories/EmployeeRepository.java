package com.ali.bugtracker.repositories;

import com.ali.bugtracker.entities.Employee;
import org.springframework.data.repository.CrudRepository;

public interface EmployeeRepository extends CrudRepository<Employee,Long> {
}
