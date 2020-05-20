package com.ali.bugtracker.controllers;


import com.ali.bugtracker.entities.*;
import com.ali.bugtracker.repositories.*;
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
    @Autowired
    CommentRepository commentRepo;
    @Autowired
    HistoryRepository historyRepo;
    //display main board for current manager
    @GetMapping()
    public String displayManagerBoard(Model model,Principal principal){
        Employee currentManager=employeeRepo.findByEmail(principal.getName());
        List<Project> projects= projectRepo.findAllByOwner(currentManager);
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
            project.setStatus("NOT STARTED");
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
                model.addAttribute("project",project);
                List<Ticket> tickets=project.getTickets();
                model.addAttribute("tickets",tickets);
                List<Ticket>uncompletedTickets=new ArrayList<>();
                for(Ticket ticket:tickets){
                    if (!ticket.getStatus().equals("COMPLETED"))
                        uncompletedTickets.add(ticket);
                }
                Ticket ticket =new Ticket();
                model.addAttribute("ticket",ticket);
                List<Employee> allEmployees=project.getEmployees();
                model.addAttribute("allEmployees",allEmployees);
                model.addAttribute("uncompletedTickets",uncompletedTickets);
                return "projects/project-details-manager";
            }
            else return "redirect:/board/manager";
        }
        else return "redirect:/board/manager";
    }
    // save a new ticket to a project
    @PostMapping("/projects/{projectId}/tickets/save")
    public String saveTicket(@PathVariable Long projectId, @Valid Ticket ticket, BindingResult bindingResult,Principal principal,Model model){
        Project project= projectRepo.findByProjectId(projectId);
        if (bindingResult.hasErrors()){
           List<Ticket> tickets=project.getTickets();
           model.addAttribute("tickets",tickets);
           List<Employee> allEmployees=project.getEmployees();
           model.addAttribute("allEmployees",allEmployees);
           return "/projects/project-details-manager";
       }
       else {
           Employee currentManager = employeeRepo.findByEmail(principal.getName());
           // ticket fields
           ticket.setOwner(currentManager);
           ticket.setStatus("NOT STARTED");
           ticket.setProjectId(project);
           ticket.setCreationDate(date);
           // history fields
           History history=new History();
           history.setEmployeeId(currentManager);
           history.setTicketId(ticket);
           history.setModificationDate(date);
           history.setEvent("CREATED");
           ticketRepo.save(ticket);
           historyRepo.save(history);
           return "redirect:/board/manager/projects/{projectId}?success";
       }

    }
    // display the details of each ticket
    @GetMapping("/projects/{projectId}/tickets/{ticketId}")
    public String displayTicketDetails(@PathVariable Long projectId,@PathVariable Long ticketId,Model model){
        Ticket ticket=ticketRepo.findTicketByTicketId(ticketId);
        Employee employee=ticket.getEmployeeId();
        String ticketAssignedTo=employee.getFirstName()+" "+employee.getLastName();
        List<Comment> comments=ticket.getComments();
        model.addAttribute("ticket",ticket);
        model.addAttribute("ticketAssignedTo",ticketAssignedTo);
        model.addAttribute("comments",comments);
        return "projects/ticket-details";
    }

    // start a project
    @GetMapping("/projects/start")
    public String startProject(@RequestParam("prId") Long projectId){
        Project project=projectRepo.findByProjectId(projectId);
        project.setStatus("IN PROGRESS");
        projectRepo.save(project);
        return "redirect:/board/manager/projects/"+projectId;
    }
    // edit project details
    @GetMapping("/projects/delete")
    public String editProject(@RequestParam("prId") Long projectId,Principal principal){
        Project project=projectRepo.findByProjectId(projectId);
        // prevent manager from deleting other managers projects
        Employee projectOwner=project.getOwner();
        Employee currentManager=employeeRepo.findByEmail(principal.getName());
        if (projectOwner!=currentManager){
            return "redirect:/board/manager?error";
        }
        else {
            projectRepo.delete(project);
            return "redirect:/board/manager";
        }
    }

    // ajax test
    @GetMapping("/job")
    public String getEmployeeName(Principal principal,Model model){
        model.addAttribute("TheName",principal.getName());
        return "projects/test :: test";
    }
}

