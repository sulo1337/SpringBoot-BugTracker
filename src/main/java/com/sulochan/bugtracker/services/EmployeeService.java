package com.sulochan.bugtracker.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sulochan.bugtracker.dto.EmployeeProject;
import com.sulochan.bugtracker.entities.Employee;
import com.sulochan.bugtracker.repositories.EmployeeRepository;

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
    public List<EmployeeProject> employeeProjects(){
        return empRepo.employeeProjects();
    }
    public Employee findByEmployeeId(Long employeeId) {
        return empRepo.findByEmployeeId(employeeId);
    }

}