package com.ali.bugtracker.controllers;


import com.ali.bugtracker.dto.ChartData;
import com.ali.bugtracker.dto.EmployeeProject;
import com.ali.bugtracker.entities.Employee;
import com.ali.bugtracker.entities.Project;
import com.ali.bugtracker.repositories.EmployeeRepository;
import com.ali.bugtracker.repositories.ProjectRepository;
import com.ali.bugtracker.services.BugService;
import com.ali.bugtracker.services.EmployeeService;
import com.ali.bugtracker.services.ProjectService;
import com.ali.bugtracker.services.TicketService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@Controller()
@RequestMapping("/profile")
public class ProfileController {

    @Autowired
    EmployeeService employeeService;
    @Autowired
    ProjectService projectService;
    @Autowired
    TicketService ticketService;
    @Autowired
    BugService bugService;

    @GetMapping()
    public String displayProfile(Principal principal, Model model) throws JsonProcessingException {
        Employee currentUser=employeeService.findByEmail(principal.getName());
        String roleCode=currentUser.getRole();
        String role= "";
        switch (roleCode){
            case "ROLE_A":
                role="admin";
                break;
            case "ROLE_M":
                role="Project Manager";
                break;
            case "ROLE_P":
                role="Programmer";
                break;
            case "ROLE_T":
                role="Tester";
                break;
        }
        if (roleCode.equals("ROLE_M")){
            Long numberOfManagerProjects=projectService.countAllByOwner(currentUser);
            Long numberOfManagerTickets=ticketService.countTicketsByOwner(currentUser);
            model.addAttribute("numberOfManagerProjects",numberOfManagerProjects);
            model.addAttribute("numberOfManagerTickets",numberOfManagerTickets);

        }
        model.addAttribute("role",role);
        model.addAttribute("currentUser",currentUser);
        displayPieChart(principal,model);

        return "profiles/profile";
    }
    public void displayPieChart(Principal principal,Model model) throws JsonProcessingException {
        Employee employee=employeeService.findByEmail(principal.getName());
        List<EmployeeProject> employeesProjectCount= employeeService.employeeProjects();
        List<ChartData> chartData=new ArrayList<>();
        switch (employee.getRole()){
            case "ROLE_M":
                chartData = projectService.getProjectStatus(employee.getEmployeeId());
                break;
            case "ROLE_P":
                chartData = ticketService.getTicketStatus(employee.getEmployeeId());
                break;
            case "ROLE_T":
                chartData = bugService.getBugSeverity(employee.getEmployeeId());
                break;
        }

        //// convert chart data to JSON to use in JS script
        ObjectMapper objectMapper=new ObjectMapper();
        String jsonString=objectMapper.writeValueAsString(chartData);
        //label,value: [["Not Started",1],["In progress",2],["Completed",5]]
        model.addAttribute("StatusCount",jsonString);
        model.addAttribute("employeesProjectCount",employeesProjectCount);
    }
}
