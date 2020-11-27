package com.ali.bugtracker.controllers;


import com.ali.bugtracker.entities.*;
import com.ali.bugtracker.services.*;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.security.Principal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Controller
@RequestMapping("/board/manager")
public class ManagerController {
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
        HashMap<Long,Long> projectUncompletedTickets=new HashMap<>();
        for (Project project:projects){
            Long count=0L;
            List<Ticket> tickets=project.getTickets();
            for (Ticket ticket:tickets){
                if (!ticket.getStatus().equals("COMPLETED")||ticket.getStatus()!=null){
                    count++;
                }
            }
            projectUncompletedTickets.put(project.getProjectId(),count);
        }
        String managerName=currentManager.getFirstName()+"'s";
        model.addAttribute("projects", projects);
        model.addAttribute("managerName",managerName);
        model.addAttribute("projectUncompletedTickets",projectUncompletedTickets);
        return "boards/manager-board";
    }

    // create a new project form
    @GetMapping("/projects/new")
    public String addNewProject(Model model) {
        Project project = new Project();
        List<Employee> programmerEmployees = employeeService.findAllByRoles("ROLE_P");
        List<Employee> testerEmployees = employeeService.findAllByRoles("ROLE_T");
        model.addAttribute("programmerEmployees", programmerEmployees);
        model.addAttribute("testerEmployees", testerEmployees);
        model.addAttribute("newOrOld","new");
        model.addAttribute("project", project);
        return "projects/project-form";
    }


    // save a project
    @PostMapping("/projects/save")
    public String saveProject(@Valid Project project, BindingResult bindingResult, Model model, Principal principal,RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            List<Employee> programmerEmployees = employeeService.findAllByRoles("ROLE_P");
            List<Employee> testerEmployees = employeeService.findAllByRoles("ROLE_T");
            model.addAttribute("programmerEmployees", programmerEmployees);
            model.addAttribute("testerEmployees", testerEmployees);
            model.addAttribute("newOrOld", project.getStatus() != null ? "old" : "new");
            return "projects/project-form";
        } else {
            Employee owner = employeeService.findByEmail(principal.getName());
            project.setOwner(owner);
            System.out.println(project.getStatus());
            projectService.save(project);
            redirectAttributes.addFlashAttribute("projectName",project.getName());
            redirectAttributes.addFlashAttribute("projectSaved",true);
            return "redirect:/board/manager?success";
        }
    }

    //show project details for current manager
    @GetMapping("/projects/{projectId}")
    public String displayProjectDetails(@PathVariable  Long projectId, Principal principal, Model model) {
        Project project = projectService.findByProjectId(projectId);
        if (project != null) {
            Employee managerOfThisProject = project.getOwner();
            Employee currentManager = employeeService.findByEmail(principal.getName());
            // check project owner first
            if (managerOfThisProject.getEmployeeId() == currentManager.getEmployeeId()) {
                model.addAttribute("project", project);
                List<Ticket> tickets = project.getTickets();
                model.addAttribute("tickets", tickets);
                List<Ticket> uncompletedTickets = new ArrayList<>();
                for (Ticket ticket : tickets) {
                    if (!ticket.getStatus().equals("COMPLETED")||ticket.getStatus()!=null){
                        uncompletedTickets.add(ticket);}
                }
                Ticket ticket = new Ticket();
                model.addAttribute("ticket", ticket);
                model.addAttribute("newOrOld","new");
                List<Employee> allEmployees = projectService.findProgrammerByProject(projectId);
                model.addAttribute("allEmployees", allEmployees);
                model.addAttribute("uncompletedTickets", uncompletedTickets);
                return "projects/project-details-manager";
            } else return "redirect:/board/manager?error";
        } else return "redirect:/board/manager?error";
    }

    // save a new ticket to a project
    @PostMapping("/projects/{projectId}/tickets/save")
    public String saveTicket(@PathVariable Long projectId, @Valid Ticket ticket, BindingResult bindingResult, Principal principal, Model model,RedirectAttributes redirectAttributes) {
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
            model.addAttribute("newOrOld", ticket.getStatus() != null ? "old" : "new");
            return "projects/project-details-manager";
        } else {
            Employee currentManager = employeeService.findByEmail(principal.getName());
            // ticket fields
            ticket.setOwner(currentManager);
            ticket.setProjectId(project);
            // history fields
            History history = new History();
            history.setEmployeeId(currentManager);
            history.setTicketId(ticket);
            history.setModificationDate(historyService.currentDate());
            history.setEvent("CREATED");
            ticketService.save(ticket);
            historyService.save(history);
            redirectAttributes.addFlashAttribute("ticketName",ticket.getName());
            redirectAttributes.addFlashAttribute("ticketSaved",true);
            return "redirect:/board/manager/projects/{projectId}?success";
        }

    }

    // display the details of each ticket
    @GetMapping("/projects/{projectId}/tickets/{ticketId}")
    public String displayTicketDetails(@PathVariable Long projectId, @PathVariable Long ticketId, Model model,Principal principal) {
        Project project=projectService.findByProjectId(projectId);
        Ticket ticket = ticketService.findTicketByTicketId(ticketId);
        if (project!=null && ticket!=null){
            Employee currentManager=employeeService.findByEmail(principal.getName());
            Employee projectOwner=project.getOwner();
            if (currentManager.getEmployeeId()==projectOwner.getEmployeeId()){
                List<Comment> comments = ticket.getComments();
                List<Bug> bugs = ticket.getBugs();
                model.addAttribute("ticket", ticket);
                model.addAttribute("comments", comments);
                model.addAttribute("bugs", bugs);
                return "tickets/ticket-details";
            } else return "redirect:/board/manager?error";
        } else return "redirect:/board/manager?error";

    }

    // start a project
    @GetMapping("/projects/start")
    public String startProject(@RequestParam("prId") Long projectId, Principal principal, RedirectAttributes redirectAttributes) {
        Project project = projectService.findByProjectId(projectId);
        if (project != null) {
            // prevent manager from starting other managers projects
            Employee projectOwner = project.getOwner();
            Employee currentManager = employeeService.findByEmail(principal.getName());
            if (projectOwner != currentManager || project.getStatus().equals("IN PROGRESS")) {
                redirectAttributes.addFlashAttribute("ownerOrStsError",true);
                return "redirect:/board/manager?error";
            } else {
                project.setStatus("IN PROGRESS");
                projectService.save(project);
                redirectAttributes.addFlashAttribute("projectStarted",true);
                return "redirect:/board/manager/projects/" + projectId;
            }
        } else return "redirect:/board/manager?error";
    }
    // mark a project as completed
    @GetMapping("/projects/complete")
    public String completeProject(@RequestParam("prId") Long projectId, Principal principal, RedirectAttributes redirectAttributes) {
        Project project = projectService.findByProjectId(projectId);
        if (project != null) {
            // prevent manager from starting other managers projects
            Employee projectOwner = project.getOwner();
            Employee currentManager = employeeService.findByEmail(principal.getName());
            if (projectOwner != currentManager || project.getStatus().equals("COMPLETED")) {
                redirectAttributes.addFlashAttribute("ownerOrStsError",true);
                return "redirect:/board/manager?error";
            } else {
                project.setStatus("COMPLETED");
                project.getTickets().forEach(ticket -> {ticket.setStatus("COMPLETED");});
                projectService.save(project);
                redirectAttributes.addFlashAttribute("projectCompleted",true);
                return "redirect:/board/manager/projects/" + projectId;
            }
        } else return "redirect:/board/manager?error";
    }

    // delete a project
    @GetMapping("/projects/delete")
    public String deleteProject(@RequestParam("prId") Long projectId, Principal principal,RedirectAttributes redirectAttributes) {
        Project project = projectService.findByProjectId(projectId);
        if (project != null) {
            // prevent manager from deleting other managers projects
            Employee projectOwner = project.getOwner();
            Employee currentManager = employeeService.findByEmail(principal.getName());
            if (projectOwner != currentManager) {
                return "redirect:/board/manager?error";
            } else {
                projectService.delete(project);
                redirectAttributes.addFlashAttribute("projectName",project.getName());
                redirectAttributes.addFlashAttribute("projectDeleted",true);
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
                return "projects/project-form";
            }
        } else return "redirect:/board/manager?error";
    }

    //delete a ticket
    @GetMapping("/projects/{projectId}/tickets/delete")
    public String deleteTicket(@RequestParam("ticketId") Long ticketId, @PathVariable Long projectId, Principal principal,RedirectAttributes redirectAttributes){
        Ticket ticket = ticketService.findTicketByTicketId(ticketId);
        if (ticket != null) {
            // prevent manager from deleting other managers tickets
            Employee ticketOwner = ticket.getOwner();
            Employee currentManager = employeeService.findByEmail(principal.getName());
            if (ticketOwner != currentManager||ticket.getProjectId().getStatus().equals("COMPLETED")) {
                redirectAttributes.addFlashAttribute("ticketError",true);
                return "redirect:/board/manager?error";
            } else {
                ticketService.delete(ticket);
                redirectAttributes.addFlashAttribute("ticketName",ticket.getName());
                redirectAttributes.addFlashAttribute("ticketDeleted",true);
                return "redirect:/board/manager/projects/{projectId}";
            }
        } else return "redirect:/board/manager?error";
    }
    //edit a ticket
    @GetMapping("/projects/{projectId}/tickets/update")
    public String editTicket(@RequestParam("ticketId") Long ticketId, @PathVariable Long projectId, Principal principal,Model model,RedirectAttributes redirectAttributes){
        Ticket ticket = ticketService.findTicketByTicketId(ticketId);
        if (ticket != null) {
            // prevent manager from editing other managers tickets
            Employee ticketOwner = ticket.getOwner();
            Employee currentManager = employeeService.findByEmail(principal.getName());
            if (ticketOwner != currentManager||ticket.getProjectId().getStatus().equals("COMPLETED")) {
                redirectAttributes.addFlashAttribute("ticketError",true);
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
                return "projects/project-details-manager";
            }
        } else return "redirect:/board/manager?error";
    }
}

