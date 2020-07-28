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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Controller
@RequestMapping("board/programmer")
public class ProgrammerController {

    //needed Repositories
    @Autowired
    ProjectService projectService;
    @Autowired
    EmployeeService employeeService;
    @Autowired
    TicketService ticketService;
    @Autowired
    CommentService commentService;
    @Autowired
    HistoryService historyService;
    @Autowired
    BugService bugService;

    //display programmer board
    @GetMapping()
    public String displayProgrammerBoard(Model model, Principal principal) {
        Employee currentProgrammer = employeeService.findByEmail(principal.getName());
        HashMap<Long, Long> ticketsCount = new HashMap<Long, Long>();
        List<Project> allProjects = currentProgrammer.getProjects();
        // get tickets count for the programmer in  each project
        for (Project project : allProjects) {
            ticketsCount.put(project.getProjectId(), ticketService.countTicketsByProjectIdAndEmployeeId(project, currentProgrammer));

        }
        String currentProgrammerName=currentProgrammer.getFirstName()+"'s";
        model.addAttribute("currentProgrammerName",currentProgrammerName);
        model.addAttribute("currentProgrammer", currentProgrammer);
        model.addAttribute("ticketsCount", ticketsCount);
        model.addAttribute("allProjects", allProjects);
        return "boards/programmer-board";
    }

    //project details contains tickets
    @GetMapping("/projects/{projectId}")
    public String displayProjectDetails(@PathVariable Long projectId, Principal principal, Model model) {
        Project project = projectService.findByProjectId(projectId);
        if (project != null) {
            Employee currentProgrammer = employeeService.findByEmail(principal.getName());
            // prevent unassigned employee
            if (!project.getEmployees().contains(currentProgrammer)) {
                return "redirect:/board/programmer";
            } else {
                // all project tickets
                List<Ticket> allTickets = project.getTickets();
                //only current user tickets
                List<Ticket> tickets = new ArrayList<>();
                for (Ticket ticket : allTickets) {
                    if (ticket.getEmployeeId().equals(currentProgrammer)) tickets.add(ticket);
                }
                Employee owner = project.getOwner();
                String ownerName = owner.getFirstName() + " " + owner.getLastName();
                model.addAttribute("ownerName", ownerName);
                model.addAttribute("tickets", tickets);
                model.addAttribute("project", project);
                return "projects/project-details-programmer";
            }
        } else return "redirect:/board/programmer";
    }

    // ticket details
    @GetMapping("/projects/{projectId}/tickets/{ticketId}")
    public String displayTicketDetails(@PathVariable Long projectId, @PathVariable Long ticketId, Model model, Principal principal) {
        Ticket ticket = ticketService.findTicketByTicketId(ticketId);
        if (ticket != null) {
            Employee currentProgrammer = employeeService.findByEmail(principal.getName());
            Employee programmerAssignedTo = ticket.getEmployeeId();
            if (currentProgrammer != programmerAssignedTo) {
                return "redirect:/board/programmer";
            } else {
                Project project = ticket.getProjectId();
                List<History> histories = ticket.getHistories();
                String startingDate = "", submissionDate = "";
                for (History history : histories) {
                    if (history.getEvent().equals("STARTED")) {
                        startingDate = history.getModificationDate();
                    }
                    if (history.getEvent().equals("SUBMITTED FOR TESTING")) {
                        submissionDate = history.getModificationDate();
                    }
                }
                model.addAttribute("startingDate", startingDate);
                model.addAttribute("submissionDate", submissionDate);

                Comment comment = new Comment();
                Employee employee = ticket.getOwner();
                String ownerName = employee.getFirstName() + " " + employee.getLastName();
                Employee assignedProgrammer = ticket.getEmployeeId();
                String ticketAssignedTo = assignedProgrammer.getFirstName() + " " + assignedProgrammer.getLastName();
                List<Comment> comments = ticket.getComments();
                List<Bug> bugs = ticket.getBugs();
                model.addAttribute("ticket", ticket);
                model.addAttribute("project", project);
                model.addAttribute("ticketAssignedTo",ticketAssignedTo);
                model.addAttribute("ticketCreatedBy", ownerName);
                model.addAttribute("comments", comments);
                model.addAttribute("bugs", bugs);
                model.addAttribute("comment", comment);
                return "tickets/ticket-details";
            }
        } else return "redirect:/board/programmer";

    }

    // add a new comment to the ticket
    @PostMapping("/projects/{projectId}/tickets/{ticketId}/comments/save")
    public String saveComment(@Valid Comment comment, BindingResult bindingResult, Principal principal, @PathVariable Long projectId, @PathVariable Long ticketId, Model model) {
        if (bindingResult.hasErrors()) {
            return "redirect:/board/programmer/projects/{projectId}/tickets/{ticketId}?error";
        } else {
            Employee currentEmployee = employeeService.findByEmail(principal.getName());
            Ticket ticket = ticketService.findTicketByTicketId(ticketId);
            // comment.setCreationDate(date);
            comment.setEmployeeId(currentEmployee);
            comment.setTicketId(ticket);
            commentService.save(comment);
            return "redirect:/board/programmer/projects/{projectId}/tickets/{ticketId}?success";
        }
    }

    // start a  ticket
    @GetMapping("/projects/tickets/start")
    public String startProject(@RequestParam("prId") Long projectId, @RequestParam("tId") Long ticketId, Principal principal) {
        Ticket ticket = ticketService.findTicketByTicketId(ticketId);
        if (ticket != null) {
            Employee currentProgrammer = employeeService.findByEmail(principal.getName());
            Employee programmerAssignedTo = ticket.getEmployeeId();
            if (currentProgrammer != programmerAssignedTo) {
                return "redirect:/board/programmer";
            } else {
                if (ticket.getStatus().equals("NOT STARTED")){
                ticket.setStatus("IN PROGRESS");
                // history fields
                History history = new History();
                history.setTicketId(ticket);
                history.setEmployeeId(currentProgrammer);
                history.setEvent("STARTED");
                history.setModificationDate(historyService.currentDate());
                // save changes
                ticketService.save(ticket);
                historyService.save(history);
                return "redirect:/board/programmer/projects/" + projectId + "/tickets/" + ticketId;
                }
                else if (ticket.getStatus().equals("COMPLETED")){
                    ticket.setStatus("SUBMITTED FOR TESTING");
                    // history fields
                    History history = new History();
                    history.setTicketId(ticket);
                    history.setEmployeeId(currentProgrammer);
                    history.setEvent("REOPENED");
                    history.setModificationDate(historyService.currentDate());
                    // save changes
                    ticketService.save(ticket);
                    historyService.save(history);
                    return "redirect:/board/programmer/projects/" + projectId + "/tickets/" + ticketId;
                }
                else return "redirect:/board/programmer/projects/" + projectId + "/tickets/" + ticketId;
            }
        } else return "redirect:/board/programmer";
    }

    //submit for testing a ticket
    @GetMapping("/projects/tickets/submitToTest")
    public String submitTicketToTest(@RequestParam("prId") Long projectId, @RequestParam("tId") Long ticketId, Principal principal) {
        Ticket ticket = ticketService.findTicketByTicketId(ticketId);
        if (ticket != null) {
            Employee currentProgrammer = employeeService.findByEmail(principal.getName());
            Employee programmerAssignedTo = ticket.getEmployeeId();
            if (currentProgrammer != programmerAssignedTo) {
                return "redirect:/board/programmer";
            } else {
                if (ticket.getStatus().equals("IN PROGRESS")){
                ticket.setStatus("SUBMITTED FOR TESTING");
                // history fields
                History history = new History();
                history.setTicketId(ticket);
                history.setEmployeeId(currentProgrammer);
                history.setEvent("SUBMITTED FOR TESTING");
                history.setModificationDate(historyService.currentDate());
                // save changes
                ticketService.save(ticket);
                historyService.save(history);
                return "redirect:/board/programmer/projects/" + projectId + "/tickets/" + ticketId;
                }
                else return "redirect:/board/programmer/projects/" + projectId + "/tickets/" + ticketId;
            }
        } else return "redirect:/board/programmer";
    }

    @GetMapping("/projects/tickets/complete")
    public String completeTicket(@RequestParam("prId") Long projectId, @RequestParam("tId") Long ticketId, Principal principal) {
        Ticket ticket = ticketService.findTicketByTicketId(ticketId);
        if (ticket != null && ticket.getStatus().equals("SUBMITTED FOR TESTING")) {
            Employee currentProgrammer = employeeService.findByEmail(principal.getName());
            Employee programmerAssignedTo = ticket.getEmployeeId();
            if (currentProgrammer != programmerAssignedTo) {
                return "redirect:/board/programmer";
            } else {
                    ticket.setStatus("COMPLETED");
                    // history fields
                    History history = new History();
                    history.setTicketId(ticket);
                    history.setEmployeeId(currentProgrammer);
                    history.setEvent("COMPLETED");
                    history.setModificationDate(historyService.currentDate());
                    // save changes
                    ticketService.save(ticket);
                    historyService.save(history);
                    return "redirect:/board/programmer/projects/" + projectId + "/tickets/" + ticketId;
            }
        } else return "redirect:/board/programmer";


    }

    // display bug details page
    @GetMapping("/projects/{projectId}/tickets/{ticketId}/bugs/{bugId}")
    public String displayBugDetails(@PathVariable Long projectId, @PathVariable Long ticketId, @PathVariable Long bugId, Model model, Principal principal) {
        Ticket ticket = ticketService.findTicketByTicketId(ticketId);
        Bug bug = bugService.findBugByBugId(bugId);
        if (ticket != null && bug != null) {
            Employee currentProgrammer = employeeService.findByEmail(principal.getName());
            Employee programmerAssignedTo = ticket.getEmployeeId();
            if (currentProgrammer != programmerAssignedTo) {
                return "redirect:/board/programmer";
            } else {
                Employee createdBy = bug.getEmployeeId();
                String testerName = createdBy.getFirstName() + " " + createdBy.getLastName();
                model.addAttribute("testerName", testerName);
                model.addAttribute("bug", bug);
                return "tickets/bug-details";
            }
        } else return "redirect:/board/programmer";
    }

    @GetMapping("/projects/tickets/bugs/fix")
    public String fixBug(@RequestParam("prId") Long projectId, @RequestParam("tId") Long ticketId, @RequestParam("bugId") Long bugId, Principal principal) {
        Ticket ticket = ticketService.findTicketByTicketId(ticketId);
        Bug bug = bugService.findBugByBugId(bugId);
        if (ticket != null && bug != null) {
            Employee currentProgrammer = employeeService.findByEmail(principal.getName());
            Employee programmerAssignedTo = ticket.getEmployeeId();
            if (currentProgrammer != programmerAssignedTo) {
                return "redirect:/board/programmer";
            } else {
                bug.setFixed(true);
                bugService.save(bug);
                return "redirect:/board/programmer/projects/" + projectId + "/tickets/" + ticketId+"/bugs/"+bugId;
            }
        } else return "redirect:/board/programmer";
    }

}
