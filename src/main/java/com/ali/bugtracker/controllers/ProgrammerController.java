package com.ali.bugtracker.controllers;


import com.ali.bugtracker.entities.*;
import com.ali.bugtracker.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.filter.HiddenHttpMethodFilter;

import javax.validation.Valid;
import java.security.Principal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Controller
@RequestMapping("board/programmer")
public class ProgrammerController {
    // date variable for creation date assigning
    DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm");
    LocalDateTime now = LocalDateTime.now();
    String date=dtf.format(now);
    //needed Repositories
    @Autowired
    ProjectRepository projectRepo;
    @Autowired
    EmployeeRepository employeeRepo;
    @Autowired
    TicketRepository ticketRepo;
    @Autowired
    CommentRepository commentRepo;
    @Autowired
    HistoryRepository historyRepo;

    //display programmer board
    @GetMapping()
    public String displayProgrammerBoard(Model model, Principal principal){
        Employee currentProgrammer=employeeRepo.findByEmail(principal.getName());
        HashMap<Long, Long> ticketsCount = new HashMap<Long,Long>();
        List<Project> allProjects=currentProgrammer.getProjects();
       // get tickets count for the programmer in  each project
        for(Project project : allProjects){
            ticketsCount.put(project.getProjectId(),
                    ticketRepo.countTicketsByProjectIdAndEmployeeId(project,currentProgrammer));

        }
        model.addAttribute("currentProgrammer",currentProgrammer);
        model.addAttribute("ticketsCount",ticketsCount);
        model.addAttribute("allProjects",allProjects);
        return "boards/programmer-board";
    }
    //project details contains tickets
    @GetMapping("/projects/{projectId}")
    public String displayProjectDetails(@PathVariable Long projectId,Principal principal,Model model){
        Project project=projectRepo.findByProjectId(projectId);
        Employee currentEmployee=employeeRepo.findByEmail(principal.getName());
        // all project tickets
        List<Ticket> allTickets=project.getTickets();
        //only current user tickets
        List<Ticket> tickets =new ArrayList<>();
        for(Ticket ticket:allTickets){
            if(ticket.getEmployeeId().equals(currentEmployee))
                tickets.add(ticket);
        }
        Employee owner=project.getOwner();
        String ownerName=owner.getFirstName()+" "+owner.getLastName();
        model.addAttribute("ownerName",ownerName);
        model.addAttribute("tickets",tickets);
        model.addAttribute("project",project);
        return "projects/project-details-programmer";
    }
    // ticket details
    @GetMapping("/projects/{projectId}/tickets/{ticketId}")
    public String displayTicketDetails(@PathVariable Long projectId,@PathVariable Long ticketId,Model model ){
       Ticket ticket= ticketRepo.findTicketByTicketId(ticketId);
       Project project=ticket.getProjectId();
       List<History> histories=ticket.getHistories();
       String startingDate="",submissionDate="";
       for (History history: histories ){
           if (history.getEvent().equals("STARTED")){
               startingDate=history.getModificationDate();
           }
           if(history.getEvent().equals("SUBMITTED FOR TESTING")){
               submissionDate=history.getModificationDate();
           }
       }
           model.addAttribute("startingDate",startingDate);
           model.addAttribute("submissionDate",submissionDate);

       Comment comment=new Comment();
       Employee employee=ticket.getOwner();
       String ownerName=employee.getFirstName()+" "+employee.getLastName();
       List<Comment> comments=ticket.getComments();
       model.addAttribute("ticket",ticket);
       model.addAttribute("project",project);
       model.addAttribute("ticketCreatedBy",ownerName);
       model.addAttribute("comments",comments);
       model.addAttribute("comment",comment);
       return "projects/ticket-details";

    }
    // add a new comment to the ticket
    @PostMapping("/projects/{projectId}/tickets/{ticketId}/comments/save")
    public String saveComment(@Valid Comment comment, BindingResult bindingResult, Principal principal, @PathVariable Long projectId, @PathVariable Long ticketId, Model model){
        if (bindingResult.hasErrors()) {
            return "redirect:/board/programmer/projects/{projectId}/tickets/{ticketId}?error";
        }
        else{
        Employee currentEmployee=employeeRepo.findByEmail(principal.getName());
        Ticket ticket= ticketRepo.findTicketByTicketId(ticketId);
        comment.setCreationDate(date);
        comment.setEmployeeId(currentEmployee);
        comment.setTicketId(ticket);
        commentRepo.save(comment);
        return "redirect:/board/programmer/projects/{projectId}/tickets/{ticketId}?success";
       }
    }

    // start a  ticket
    @PostMapping("/projects/{projectId}/tickets/{ticketId}/start")
    public String startTicket(@PathVariable Long projectId,@PathVariable Long ticketId,Principal principal){
        Employee currentProgrammer=employeeRepo.findByEmail(principal.getName());
        Ticket ticket=ticketRepo.findTicketByTicketId(ticketId);
        ticket.setStatus("IN PROGRESS");
        // history fields
        History history=new History();
        history.setTicketId(ticket);
        history.setEmployeeId(currentProgrammer);
        history.setEvent("STARTED");
        history.setModificationDate(date);
        // save changes
        ticketRepo.save(ticket);
        historyRepo.save(history);
        return "redirect:/board/programmer/projects/{projectId}/tickets/{ticketId}";
    }
    @PostMapping("/projects/{projectId}/tickets/{ticketId}/submitToTest")
    public String submitToTest(@PathVariable Long projectId,@PathVariable Long ticketId,Principal principal){
        Employee currentProgrammer=employeeRepo.findByEmail(principal.getName());
        Ticket ticket=ticketRepo.findTicketByTicketId(ticketId);
        ticket.setStatus("SUBMITTED FOR TESTING");
        // history fields
        History history=new History();
        history.setTicketId(ticket);
        history.setEmployeeId(currentProgrammer);
        history.setEvent("SUBMITTED FOR TESTING");
        history.setModificationDate(date);
        // save changes
        ticketRepo.save(ticket);
        historyRepo.save(history);
        return "redirect:/board/programmer/projects/{projectId}/tickets/{ticketId}";
    }

}
