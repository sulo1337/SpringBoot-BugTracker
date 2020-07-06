package com.ali.bugtracker.services;

import com.ali.bugtracker.entities.Employee;
import com.ali.bugtracker.entities.Project;
import com.ali.bugtracker.repositories.ProjectRepository;
import com.sun.org.apache.bcel.internal.generic.IF_ACMPEQ;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ProjectService {

    @Autowired
    ProjectRepository projectRepo;

    public List<Project> findAll(){
        return projectRepo.findAll();
    }
    public List<Project> findAllByOwner(Employee manager){
    return projectRepo.findAllByOwnerOrderByDeadline(manager);
    }
    public Project findByName(String name){ return projectRepo.findByName(name); }
    public Project findByProjectId(Long id){return projectRepo.findByProjectId(id);}
    public void save(Project project){projectRepo.save(project);}
    public void delete(Project project){projectRepo.delete(project);}

    public List<Employee> findProgrammerByProject(Long projectId){
         Project project=projectRepo.findByProjectId(projectId);
         List<Employee> allEmployees=project.getEmployees();
         List<Employee> programmers=new ArrayList<>();
         try {

             for (Employee programmer: allEmployees){
                 if (programmer.getRole().equals("ROLE_P")){
                     programmers.add(programmer);
                 }
             }
         }
         catch (NullPointerException ex)
         {
             ex.printStackTrace();
             System.out.println("all employees list might be empty, selected project might have zero employees");
         }
        return programmers; }

}
