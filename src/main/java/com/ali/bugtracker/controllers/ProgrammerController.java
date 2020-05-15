package com.ali.bugtracker.controllers;


import com.ali.bugtracker.entities.Comment;
import com.ali.bugtracker.entities.Employee;
import com.ali.bugtracker.entities.Project;
import com.ali.bugtracker.entities.Ticket;
import com.ali.bugtracker.repositories.CommentRepository;
import com.ali.bugtracker.repositories.EmployeeRepository;
import com.ali.bugtracker.repositories.ProjectRepository;
import com.ali.bugtracker.repositories.TicketRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

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
    //display programmer board
    @GetMapping()
    public String displayProgrammerBoard(Model model, Principal principal){
        Employee currentProgrammer=employeeRepo.findByEmail(principal.getName());
        HashMap<Long, Long> ticketsCount = new HashMap<Long,Long>();
        List<Long> projectIds= projectRepo.findAllByEmployeeId(currentProgrammer.getEmployeeId());
        List<Project> allProjects=new ArrayList<>();
        for(Long projectId : projectIds){
            allProjects.add(projectRepo.findByProjectId(projectId));
            ticketsCount.put(projectId,
                    ticketRepo.countTicketsByProjectIdAndEmployeeId(projectRepo.findByProjectId(projectId),
                            currentProgrammer));

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
        List<Ticket> tickets=ticketRepo.findAllByEmployeeIdAndProjectId(currentEmployee,project);
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
       Comment comment=new Comment();
       Employee employee=ticket.getOwner();
       String ownerName=employee.getFirstName()+" "+employee.getLastName();
       List<Comment> comments=commentRepo.findAllByTicketId(ticket);
       model.addAttribute("ticket",ticket);
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

}
