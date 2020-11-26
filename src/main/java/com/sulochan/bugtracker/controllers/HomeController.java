package com.sulochan.bugtracker.controllers;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sulochan.bugtracker.dto.ChartData;
import com.sulochan.bugtracker.dto.EmployeeProject;
import com.sulochan.bugtracker.dto.TimelineData;
import com.sulochan.bugtracker.entities.Employee;
import com.sulochan.bugtracker.entities.Project;
import com.sulochan.bugtracker.repositories.EmployeeRepository;
import com.sulochan.bugtracker.repositories.ProjectRepository;

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
    public String displayHome(Model model,Principal principal){
        if (principal!=null){
            return "redirect:/profile";
        }
        return "main/index";
    }

    @GetMapping("/home")
    public String displayChartsPage(Principal principal, Model model) throws JsonProcessingException {
        Employee currentManager=empRepo.findByEmail(principal.getName());
        model.addAttribute("currentManager",currentManager);
        List<Project> ownedProjects= proRepo.findAllByOwnerOrderByDeadline(currentManager);
        model.addAttribute("projectsExists", ownedProjects.size() != 0);
        displayProjectsTimelines(currentManager,model);
        return "main/home";
    }
    public void displayProjectsTimelines(Employee employee, Model model) throws JsonProcessingException {
        List<TimelineData> timelineData=proRepo.getTimeLineData(employee.getEmployeeId());
        ObjectMapper objectMapper= new ObjectMapper();
        String jsonTimelineString =objectMapper.writeValueAsString(timelineData);
        System.out.println("-------------------project timeline---------------------");
        System.out.println(jsonTimelineString);
        model.addAttribute("projectTimeList",jsonTimelineString);
    }

}
