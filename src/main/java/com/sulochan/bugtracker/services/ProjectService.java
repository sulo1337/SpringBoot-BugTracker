package com.sulochan.bugtracker.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Service;

import com.sulochan.bugtracker.dto.ChartData;
import com.sulochan.bugtracker.entities.Employee;
import com.sulochan.bugtracker.entities.Project;
import com.sulochan.bugtracker.repositories.ProjectRepository;

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
    public Long countAllByOwner(Employee owner){ return projectRepo.countAllByOwner(owner); }
    public List<ChartData> getProjectStatus(Long ownerId){ return projectRepo.getProjectStatus(ownerId); }
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
