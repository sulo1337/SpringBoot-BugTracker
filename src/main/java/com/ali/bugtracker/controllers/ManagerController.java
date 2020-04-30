package com.ali.bugtracker.controllers;


import com.ali.bugtracker.entities.Employee;
import com.ali.bugtracker.entities.Project;
import com.ali.bugtracker.entities.Ticket;
import com.ali.bugtracker.repositories.EmployeeRepository;
import com.ali.bugtracker.repositories.ProjectRepository;
import com.ali.bugtracker.repositories.TicketRepository;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;
import java.security.Principal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/board/manager")
public class ManagerController {
    // date variable for creation date assigning
    DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm");
    LocalDateTime now = LocalDateTime.now();
    String date=dtf.format(now);
   // needed repositories
    @Autowired
    EmployeeRepository employeeRepo;
    @Autowired
    ProjectRepository projectRepo;
    @Autowired
    TicketRepository ticketRepo;

    //display main board for current manager
    @GetMapping()
    public String displayManagerBoard(Model model,Principal principal){
        Employee currentManager=employeeRepo.findByEmail(principal.getName());
        List<Project> projects= projectRepo.findAllByOwner(currentManager);
        model.addAttribute("projects",projects);
        List<Ticket> tickets=ticketRepo.findAllByOwner(currentManager);
        model.addAttribute("tickets",tickets);
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
    //show project details for current manager
    @GetMapping("/projects/{projectId}")
    public String displayProjectDetails(@PathVariable Long projectId,Principal principal,Model model){
        Project project= projectRepo.findByProjectId(projectId);
        if (project!=null){
            Employee managerOfThisProject=project.getOwner();
            Employee currentManager=employeeRepo.findByEmail(principal.getName());
            if(managerOfThisProject.getEmployeeId()==currentManager.getEmployeeId()){
                List<Ticket> tickets=ticketRepo.findAllByProjectId(project);
                model.addAttribute("tickets",tickets);
                Ticket ticket =new Ticket();
                model.addAttribute("ticket",ticket);
                List<Employee> allEmployees=new ArrayList<>();
                List<Long>employeesIds=employeeRepo.findAllByProjectId(projectId);
                for (Long employeesId : employeesIds) {
                    allEmployees.add(employeeRepo.findByEmployeeId(employeesId));
                }
                model.addAttribute("allEmployees",allEmployees);
                return "/projects/project-details";
            }
            else return "redirect:/board/manager";
        }

        else return "redirect:/board/manager";
    }
    // save a new ticket to a project
    @PostMapping("/projects/{projectId}/tickets/save")
    public String saveTicket(@PathVariable Long projectId, @Valid Ticket ticket, BindingResult bindingResult,Principal principal,Model model){
       if (bindingResult.hasErrors()){
           Project project= projectRepo.findByProjectId(projectId);
           List<Ticket> tickets=ticketRepo.findAllByProjectId(project);
           model.addAttribute("tickets",tickets);
           List<Employee> allEmployees=new ArrayList<>();
           List<Long>employeesIds=employeeRepo.findAllByProjectId(projectId);
           for (Long employeesId : employeesIds) {
               allEmployees.add(employeeRepo.findByEmployeeId(employeesId));
           }
           model.addAttribute("allEmployees",allEmployees);
           return "/projects/project-details";
       }
       else {
           Employee currentManager = employeeRepo.findByEmail(principal.getName());
           Project project = projectRepo.findByProjectId(projectId);
           ticket.setOwner(currentManager);
           ticket.setProjectId(project);
           ticket.setCreationDate(date);
           ticketRepo.save(ticket);
           return "redirect:/board/manager/projects/{projectId}?success";
       }

    }
    // display the details of each ticket
    @GetMapping("/projects/tickets/{ticketId}")
    public String displayTicketDetails(@PathVariable Long ticketId){
        return "projects/ticket-details";
    }

}

