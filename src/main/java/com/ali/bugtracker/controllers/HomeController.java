package com.ali.bugtracker.controllers;


import com.ali.bugtracker.dto.ChartData;
import com.ali.bugtracker.dto.EmployeeProject;
import com.ali.bugtracker.dto.TimelineData;
import com.ali.bugtracker.entities.Employee;
import com.ali.bugtracker.entities.Project;
import com.ali.bugtracker.repositories.EmployeeRepository;
import com.ali.bugtracker.repositories.ProjectRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.security.Principal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller()
@RequestMapping("/")
public class HomeController {

    @Autowired
    EmployeeRepository empRepo;

    @Autowired
    ProjectRepository proRepo;

    @GetMapping()
    public String displayHome(){
        return "main/index";
    }

    @GetMapping("/home")
    public String displayChartsPage(Principal principal, Model model) throws JsonProcessingException {
        displayProjectsTimelines(principal,model);
        return "main/home";
    }
    public void displayProjectsTimelines(Principal principal, Model model) throws JsonProcessingException {
        Employee employee=empRepo.findByEmail(principal.getName());
        List<TimelineData> timelineData=proRepo.getTimeLineData(employee.getEmployeeId());
        ObjectMapper objectMapper= new ObjectMapper();
        String jsonTimelineString =objectMapper.writeValueAsString(timelineData);
        System.out.println("-------------------project timeline---------------------");
        System.out.println(jsonTimelineString);
        model.addAttribute("projectTimeList",jsonTimelineString);
    }

}
