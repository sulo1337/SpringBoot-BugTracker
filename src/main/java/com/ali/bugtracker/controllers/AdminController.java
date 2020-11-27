package com.ali.bugtracker.controllers;

import com.ali.bugtracker.entities.*;
import com.ali.bugtracker.services.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/board/admin")
public class AdminController {
    @Autowired
    EmployeeService employeeService;
    @Autowired
    ProjectService projectService;
    @Autowired
    TicketService ticketService;
    @Autowired
    BugService bugService;
    @Autowired
    HistoryService historyService;
    @GetMapping()

    //display main admin board
    public String displayAdminBoard(Model model) {
        List<Employee> employeeList=employeeService.findAll();
        List<Project> projectList =projectService.findAll();
        List<Ticket> ticketList =ticketService.findAll();
        List<Bug> bugList=bugService.findAll();
        List<History> historyList=historyService.findAll();
        model.addAttribute("employeeList",employeeList);
        model.addAttribute("projectList",projectList);
        model.addAttribute("ticketList",ticketList);
        model.addAttribute("bugList",bugList);
        model.addAttribute("historyList",historyList);
        return "boards/admin-board";
    }
    //delete employee
    @GetMapping("/employee/delete")
    public String deleteEmployee(@RequestParam("empId") Long employeeId, Principal principal) {
        Employee employee = employeeService.findByEmployeeId(employeeId);
        if (employee != null) {
            Employee currentAdmin = employeeService.findByEmail(principal.getName());
            if (!currentAdmin.getRole().equals("ROLE_A")) {
                return "redirect:/board/admin?error";
            } else {
                employeeService.delete(employee);
                return "redirect:/board/admin?success";
            }
        } else return "redirect:/board/admin?error";
    }
    //delete project
    @GetMapping("/project/delete")
    public String deleteProject(@RequestParam("prId") Long projectId, Principal principal) {
        Project project = projectService.findByProjectId(projectId);
        if (project != null) {
            Employee currentAdmin = employeeService.findByEmail(principal.getName());
            if (!currentAdmin.getRole().equals("ROLE_A")) {
                return "redirect:/board/admin?error";
            } else {
                projectService.delete(project);
                return "redirect:/board/admin?success";
            }
        } else return "redirect:/board/admin?error";
    }
    //delete a ticket
    @GetMapping("/ticket/delete")
    public String deleteTicket(@RequestParam("ticketId") Long ticketId, Principal principal) {
        Ticket ticket = ticketService.findTicketByTicketId(ticketId);
        if (ticket != null) {
            Employee currentAdmin = employeeService.findByEmail(principal.getName());
            if (!currentAdmin.getRole().equals("ROLE_A")) {
                return "redirect:/board/admin?error";
            } else {
                ticketService.delete(ticket);
                return "redirect:/board/admin?success";
            }
        } else return "redirect:/board/admin?error";
    }
    //delete a bug
    @GetMapping("/bug/delete")
    public String deleteBug(@RequestParam("bugId") Long bugId, Principal principal) {
        Bug bug = bugService.findBugByBugId(bugId);
        if (bug != null) {
            Employee currentAdmin = employeeService.findByEmail(principal.getName());
            if (!currentAdmin.getRole().equals("ROLE_A")) {
                return "redirect:/board/admin?error";
            } else {
                bugService.delete(bug);
                return "redirect:/board/admin?success";
            }
        } else return "redirect:/board/admin?error";
    }
    //delete a history
    @GetMapping("/history/delete")
    public String deleteHistory(@RequestParam("historyId") Long historyId, Principal principal) {
        History history = historyService.findHistoryByHistoryId(historyId);
        if (history != null) {
            Employee currentAdmin = employeeService.findByEmail(principal.getName());
            if (!currentAdmin.getRole().equals("ROLE_A")) {
                return "redirect:/board/admin?error";
            } else {
                historyService.delete(history);
                return "redirect:/board/admin?success";
            }
        } else return "redirect:/board/admin?error";
    }
}
