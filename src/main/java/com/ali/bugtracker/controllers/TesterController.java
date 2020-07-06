package com.ali.bugtracker.controllers;

import com.ali.bugtracker.entities.Bug;
import com.ali.bugtracker.entities.Employee;
import com.ali.bugtracker.entities.Project;
import com.ali.bugtracker.entities.Ticket;
import com.ali.bugtracker.services.BugService;
import com.ali.bugtracker.services.EmployeeService;
import com.ali.bugtracker.services.ProjectService;
import com.ali.bugtracker.services.TicketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/board/tester")
public class TesterController {

    @Autowired
    TicketService ticketService;
    @Autowired
    ProjectService projectService;
    @Autowired
    EmployeeService employeeService;
    @Autowired
    BugService bugService;

    // tester board page
    @GetMapping()
    public String displayTesterBoard(Principal principal, Model model) {
        Employee tester = employeeService.findByEmail(principal.getName());
        List<Project> projects = tester.getProjects();
        model.addAttribute("projects", projects);
        return "boards/tester-board";
    }

    // tester projects details
    @GetMapping("/projects/{projectId}")
    public String displayProjectDetails(@PathVariable Long projectId, Principal principal, Model model) {
        Project project = projectService.findByProjectId(projectId);
        if (project != null) {
            Employee currentTester = employeeService.findByEmail(principal.getName());
            // prevent unassigned employee
            if (!project.getEmployees().contains(currentTester)) {
                return "redirect:/board/tester";
            } else {
                // all project tickets
                List<Ticket> allTickets = project.getTickets();
                //only submitted for testing tickets
                List<Ticket> submittedTickets = new ArrayList<>();
                for (Ticket ticket : allTickets) {
                    if (ticket.getStatus().equals("SUBMITTED FOR TESTING")) submittedTickets.add(ticket);
                }
                Employee owner = project.getOwner();
                String ownerName = owner.getFirstName() + " " + owner.getLastName();
                model.addAttribute("ownerName", ownerName);
                model.addAttribute("submittedTickets", submittedTickets);
                model.addAttribute("project", project);
                return "projects/project-details-tester";
            }
        } else return "redirect:/board/tester";
    }

    //ticket details and you can add new bugs ....
    @GetMapping("/projects/{projectId}/tickets/{ticketId}")
    public String displayTicketDetails(@PathVariable Long projectId, @PathVariable Long ticketId, Principal principal, Model model) {
        Project project = projectService.findByProjectId(projectId);
        Ticket ticket = ticketService.findTicketByTicketId(ticketId);
        if (project != null && ticket!=null) {
            Employee currentTester = employeeService.findByEmail(principal.getName());
            // prevent tester from going to other testers tickets
            if (!project.getEmployees().contains(currentTester) || !ticket.getStatus().equals("SUBMITTED FOR TESTING")) {
                return "redirect:/board/tester";
            } else {
                List<Bug> allBugs = bugService.findBugsByTicketId(ticket);
                Bug bug = new Bug();
                model.addAttribute("bug", bug);
                model.addAttribute("ticket", ticket);
                model.addAttribute("allBugs", allBugs);
                return "projects/ticket-details-tester";
            }
        } else return "redirect:/board/tester";
    }

    @PostMapping("/projects/{projectId}/tickets/{ticketId}/bug/save")
    public String addNewBug(@Valid Bug bug, BindingResult bindingResult, Model model, Principal principal, @PathVariable Long projectId, @PathVariable Long ticketId) {
        Project project = projectService.findByProjectId(projectId);
        Ticket ticket = ticketService.findTicketByTicketId(ticketId);
        if (project != null && ticket!=null ) {
            Employee currentTester = employeeService.findByEmail(principal.getName());
            // prevent tester from saving bugs in other testers tickets
           if (!project.getEmployees().contains(currentTester) || !ticket.getStatus().equals("SUBMITTED FOR TESTING")){
               return "redirect:/board/tester";
           }
           else {
               if (bindingResult.hasErrors()) {
                   List<Bug> allBugs = bugService.findBugsByTicketId(ticket);
                   model.addAttribute("ticket", ticket);
                   model.addAttribute("allBugs", allBugs);
                   return "projects/ticket-details-tester";
               } else {
                   Employee tester = employeeService.findByEmail(principal.getName());
                   bug.setEmployeeId(tester);
                   bug.setFixed(false);
                   bug.setTicketId(ticket);
                   bugService.save(bug);
                   return "redirect:/board/tester/projects/{projectId}/tickets/{ticketId}?success";
               }
           }
        } else return "redirect:/board/tester";

    }
}

