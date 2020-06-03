package com.ali.bugtracker.services;

import com.ali.bugtracker.entities.Employee;
import com.ali.bugtracker.repositories.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmployeeService {

    @Autowired
    EmployeeRepository empRepo;

    public List<Employee> findAll(){
        return empRepo.findAll();
    }
    public Employee findByEmail(String email){
        return empRepo.findByEmail(email);
    }
    public List<Employee> findAllByRoles(String role){
        return empRepo.findAllByRoles(role);
    }
    public void save(Employee employee){empRepo.save(employee); }
    public void delete(Employee employee){empRepo.delete(employee);}

    public Employee findByEmployeeId(Long employeeId) {
        return empRepo.findByEmployeeId(employeeId);
    }

}
