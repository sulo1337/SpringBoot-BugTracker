package com.ali.bugtracker.controllers;


import com.ali.bugtracker.entities.*;
import com.ali.bugtracker.services.*;
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
    String date = dtf.format(now);
    // needed repositories
    @Autowired
    EmployeeService employeeService;
    @Autowired
    ProjectService projectService;
    @Autowired
    TicketService ticketService;
    @Autowired
    CommentService commentService;
    @Autowired
    HistoryService historyService;

    //display main board for current manager
    @GetMapping()
    public String displayManagerBoard(Model model, Principal principal) {
        Employee currentManager = employeeService.findByEmail(principal.getName());
        List<Project> projects = projectService.findAllByOwner(currentManager);
        model.addAttribute("projects", projects);
        return "boards/manager-board";
    }

    // create a new project form
    @GetMapping("/projects/new")
    public String addNewProject(Model model) {
        Project project = new Project();
        List<Employee> programmerEmployees = employeeService.findAllByRoles("ROLE_P");
        List<Employee> testerEmployees = employeeService.findAllByRoles("ROLE_T");
        // List<Employee> assignedEmployees=new ArrayList<>();
       // model.addAttribute("assignedEmployees",assignedEmployees);
        model.addAttribute("programmerEmployees", programmerEmployees);
        model.addAttribute("testerEmployees", testerEmployees);
        model.addAttribute("newOrOld","new");
        model.addAttribute("project", project);
        return "/projects/project-form";
    }


    // save new project
    @PostMapping("/projects/save")
    public String saveProject(@Valid Project project, BindingResult bindingResult, Model model, Principal principal) {
        if (bindingResult.hasErrors()) {
            List<Employee> programmerEmployees = employeeService.findAllByRoles("ROLE_P");
            List<Employee> testerEmployees = employeeService.findAllByRoles("ROLE_T");
            model.addAttribute("programmerEmployees", programmerEmployees);
            model.addAttribute("testerEmployees", testerEmployees);

            return "/projects/project-form";
        } else {
            Employee owner = employeeService.findByEmail(principal.getName());
            project.setOwner(owner);

        //    project.setStatus("NOT STARTED");
      //      project.setCreationDate(date);
            projectService.save(project);
            return "redirect:/board/manager?success";
        }
    }

    //show project details for current manager
    @GetMapping("/projects/{projectId}")
    public String displayProjectDetails(@PathVariable Long projectId, Principal principal, Model model) {
        Project project = projectService.findByProjectId(projectId);
        if (project != null) {
            Employee managerOfThisProject = project.getOwner();
            Employee currentManager = employeeService.findByEmail(principal.getName());
            if (managerOfThisProject.getEmployeeId() == currentManager.getEmployeeId()) {
                model.addAttribute("project", project);
                List<Ticket> tickets = project.getTickets();
                model.addAttribute("tickets", tickets);
                List<Ticket> uncompletedTickets = new ArrayList<>();
                for (Ticket ticket : tickets) {
                    if (!ticket.getStatus().equals("COMPLETED")) uncompletedTickets.add(ticket);
                }
                Ticket ticket = new Ticket();
                model.addAttribute("ticket", ticket);
                model.addAttribute("newOrOld","new");
                List<Employee> allEmployees = projectService.findProgrammerByProject(projectId);
                model.addAttribute("allEmployees", allEmployees);
                model.addAttribute("uncompletedTickets", uncompletedTickets);
                return "projects/project-details-manager";
            } else return "redirect:/board/manager";
        } else return "redirect:/board/manager";
    }

    // save a new ticket to a project
    @PostMapping("/projects/{projectId}/tickets/save")
    public String saveTicket(@PathVariable Long projectId, @Valid Ticket ticket, BindingResult bindingResult, Principal principal, Model model) {
        Project project = projectService.findByProjectId(projectId);
        if (bindingResult.hasErrors()) {
            model.addAttribute("project", project);
            List<Ticket> tickets = project.getTickets();
            model.addAttribute("tickets", tickets);
            List<Ticket> uncompletedTickets = new ArrayList<>();
            for (Ticket nonCompletedTicket : tickets) {
                if (!nonCompletedTicket.getStatus().equals("COMPLETED")) uncompletedTickets.add(nonCompletedTicket);
            }
            model.addAttribute("uncompletedTickets", uncompletedTickets);
            List<Employee> allEmployees = project.getEmployees();
            model.addAttribute("allEmployees", allEmployees);
            return "/projects/project-details-manager";
        } else {
            Employee currentManager = employeeService.findByEmail(principal.getName());
            // ticket fields
            ticket.setOwner(currentManager);
            ticket.setProjectId(project);
        //    ticket.setCreationDate(date);
            // history fields
            History history = new History();
            history.setEmployeeId(currentManager);
            history.setTicketId(ticket);
            history.setModificationDate(date);
            history.setEvent("CREATED");
            ticketService.save(ticket);
            historyService.save(history);
            return "redirect:/board/manager/projects/{projectId}?success";
        }

    }

    // display the details of each ticket
    @GetMapping("/projects/{projectId}/tickets/{ticketId}")
    public String displayTicketDetails(@PathVariable Long projectId, @PathVariable Long ticketId, Model model) {
        Ticket ticket = ticketService.findTicketByTicketId(ticketId);
        Employee employee = ticket.getEmployeeId();
        String ticketAssignedTo = employee.getFirstName() + " " + employee.getLastName();
        List<Comment> comments = ticket.getComments();
        model.addAttribute("ticket", ticket);
        model.addAttribute("ticketAssignedTo", ticketAssignedTo);
        model.addAttribute("comments", comments);
        return "projects/ticket-details";
    }

    // start a project
    @GetMapping("/projects/start")
    public String startProject(@RequestParam("prId") Long projectId, Principal principal) {
        Project project = projectService.findByProjectId(projectId);
        if (project != null) {
            // prevent manager from starting other managers projects
            Employee projectOwner = project.getOwner();
            Employee currentManager = employeeService.findByEmail(principal.getName());
            if (projectOwner != currentManager) {
                return "redirect:/board/manager?error";
            } else {
                project.setStatus("IN PROGRESS");
                projectService.save(project);
                return "redirect:/board/manager/projects/" + projectId;
            }
        } else return "redirect:/board/manager?error";
    }

    // delete a project
    @GetMapping("/projects/delete")
    public String deleteProject(@RequestParam("prId") Long projectId, Principal principal) {
        Project project = projectService.findByProjectId(projectId);
        if (project != null) {
            // prevent manager from deleting other managers projects
            Employee projectOwner = project.getOwner();
            Employee currentManager = employeeService.findByEmail(principal.getName());
            if (projectOwner != currentManager) {
                return "redirect:/board/manager?error";
            } else {
                projectService.delete(project);
                return "redirect:/board/manager";
            }
        } else return "redirect:/board/manager?error";
    }

    // edit project details
    @GetMapping("/projects/update")
    public String editProject(@RequestParam("prId") Long projectId, Principal principal,Model model){
        Project project = projectService.findByProjectId(projectId);
        if (project != null) {
            // prevent manager from editing other managers projects
            Employee projectOwner = project.getOwner();
            Employee currentManager = employeeService.findByEmail(principal.getName());
            if (projectOwner != currentManager) {
                return "redirect:/board/manager?error";
            } else {
                List<Employee> programmerEmployees = employeeService.findAllByRoles("ROLE_P");
                List<Employee> testerEmployees = employeeService.findAllByRoles("ROLE_T");
                model.addAttribute("programmerEmployees",programmerEmployees);
                model.addAttribute("testerEmployees",testerEmployees);
                model.addAttribute("newOrOld","old");
                model.addAttribute("project",project);
                return "/projects/project-form";
            }
        } else return "redirect:/board/manager?error";
    }

    //delete a ticket
    @GetMapping("/projects/{projectId}/tickets/delete")
    public String deleteTicket(@RequestParam("ticketId") Long ticketId, @PathVariable Long projectId, Principal principal){
        Ticket ticket = ticketService.findTicketByTicketId(ticketId);
        if (ticket != null) {
            // prevent manager from deleting other managers tickets
            Employee ticketOwner = ticket.getOwner();
            Employee currentManager = employeeService.findByEmail(principal.getName());
            if (ticketOwner != currentManager) {
                return "redirect:/board/manager?error";
            } else {
                ticketService.delete(ticket);
                return "redirect:/board/manager/projects/{projectId}";
            }
        } else return "redirect:/board/manager?error";
    }
    //edit a ticket
    @GetMapping("/projects/{projectId}/tickets/update")
    public String editTicket(@RequestParam("ticketId") Long ticketId, @PathVariable Long projectId, Principal principal,Model model){
        Ticket ticket = ticketService.findTicketByTicketId(ticketId);
        if (ticket != null) {
            // prevent manager from editing other managers tickets
            Employee ticketOwner = ticket.getOwner();
            Employee currentManager = employeeService.findByEmail(principal.getName());
            if (ticketOwner != currentManager) {
                return "redirect:/board/manager?error";
            } else {
                Project project=projectService.findByProjectId(projectId);
                model.addAttribute("project",project);
                List<Ticket> tickets=project.getTickets();
                model.addAttribute("tickets",tickets);
                List<Ticket> uncompletedTickets = new ArrayList<>();
                for (Ticket nonCompletedTicket : tickets) {
                    if (!nonCompletedTicket.getStatus().equals("COMPLETED")) uncompletedTickets.add(nonCompletedTicket);
                }
                model.addAttribute("uncompletedTickets", uncompletedTickets);
                List<Employee> allEmployees = project.getEmployees();
                model.addAttribute("allEmployees", allEmployees);
                model.addAttribute("newOrOld","old");
                model.addAttribute("ticket",ticket);
                return "/projects/project-details-manager";
            }
        } else return "redirect:/board/manager?error";
    }
    // ajax test
    @GetMapping("/job")
    public String getEmployeeName(Principal principal, Model model) {
        model.addAttribute("TheName", principal.getName());
        return "projects/test :: test";
    }
}

