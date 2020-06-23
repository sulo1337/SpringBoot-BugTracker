package com.ali.bugtracker.controllers;

import com.ali.bugtracker.entities.Employee;
import com.ali.bugtracker.entities.Project;
import com.ali.bugtracker.entities.Ticket;
import com.ali.bugtracker.services.EmployeeService;
import com.ali.bugtracker.services.ProjectService;
import com.ali.bugtracker.services.TicketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

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

    // tester board page
    @GetMapping()
    public String displayTesterBoard(Principal principal, Model model){
        Employee tester=employeeService.findByEmail(principal.getName());
        List<Project> projects=tester.getProjects();
        model.addAttribute("projects",projects);
        return "boards/tester-board";
    }
    // tester projects details
    @GetMapping("/projects/{projectId}")
    public String displayProjectDetails(@PathVariable Long projectId, Principal principal, Model model){
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

}
