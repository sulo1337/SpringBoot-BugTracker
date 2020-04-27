package com.ali.bugtracker.controllers;


import com.ali.bugtracker.entities.Employee;
import com.ali.bugtracker.entities.Project;
import com.ali.bugtracker.repositories.EmployeeRepository;
import com.ali.bugtracker.repositories.ProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.jws.WebParam;
import javax.validation.Valid;
import java.security.Principal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/board/manager")
public class ManagerController {
    DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm");
    LocalDateTime now = LocalDateTime.now();
    String date=dtf.format(now);

    @Autowired
    EmployeeRepository employeeRepo;

    @Autowired
    ProjectRepository projectRepo;

    @GetMapping()
    public String displayManagerBoard(Model model){
        List<Project> projects= projectRepo.findAll();
        model.addAttribute("projects",projects);
        return "boards/manager-board";
    }

    // create a new project form
    @GetMapping("/projects/new")
    public String addNewProject(Model model){
        Project project=new Project();
        List<Employee> allEmployees=employeeRepo.findAllByRoles();
        model.addAttribute("allEmployees",allEmployees);
        model.addAttribute("project",project);
        return "projects/new-project";
    }

    // save new project
    @PostMapping("/projects/save")
    public String saveProject(@Valid Project project, BindingResult bindingResult, Model model, Principal principal){
        if (bindingResult.hasErrors()) {
            List<Employee> allEmployees=employeeRepo.findAllByRoles();
            model.addAttribute("allEmployees",allEmployees);
            return "/projects/new-project";
        }
        else {
        Employee owner= employeeRepo.findByEmail(principal.getName());
            project.setOwner(owner);
            project.setCreationDate(date);
            projectRepo.save(project);
            return "redirect:/board/manager?success";
        }
    }

    @GetMapping("/project/{projectId}")
    public String displayProjectDetails(@PathVariable Long projectId){

        return "/projects/project-details";
    }

}

