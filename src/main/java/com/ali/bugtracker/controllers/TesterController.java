package com.ali.bugtracker.controllers;

import com.ali.bugtracker.entities.Bug;
import com.ali.bugtracker.entities.Employee;
import com.ali.bugtracker.entities.Project;
import com.ali.bugtracker.entities.Ticket;
import com.ali.bugtracker.security.WebConfig;
import com.ali.bugtracker.services.BugService;
import com.ali.bugtracker.services.EmployeeService;
import com.ali.bugtracker.services.ProjectService;
import com.ali.bugtracker.services.TicketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.Principal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/board/tester")
public class TesterController {
    @Value("${uploadDir}")
    private String UPLOAD_DIR;

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
        HashMap<Long, Long> projectsTicketsToTest = new HashMap<>();
        for (Project project : projects) {
            Long count = ticketService.countTicketsByProjectIdAndStatus(project, "SUBMITTED FOR TESTING");
            projectsTicketsToTest.put(project.getProjectId(), count);
        }
        String currentTesterName = tester.getFirstName() + "'s";
        model.addAttribute("currentTester",tester);
        model.addAttribute("currentTesterName", currentTesterName);
        model.addAttribute("projectsTicketsToTest", projectsTicketsToTest);
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
        if (project != null && ticket != null) {
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
                return "tickets/ticket-details-tester";
            }
        } else return "redirect:/board/tester";
    }

    @PostMapping("/projects/{projectId}/tickets/{ticketId}/bug/save")
    public String addNewBug(@Valid Bug bug, BindingResult bindingResult, Model model, Principal principal, @PathVariable Long projectId, @PathVariable Long ticketId, @RequestParam("file") MultipartFile file, RedirectAttributes redirectAttributes) {
        Project project = projectService.findByProjectId(projectId);
        Ticket ticket = ticketService.findTicketByTicketId(ticketId);
        if (project != null && ticket != null) {
            Employee currentTester = employeeService.findByEmail(principal.getName());
            // prevent tester from saving bugs in other testers tickets
            if (!project.getEmployees().contains(currentTester) || !ticket.getStatus().equals("SUBMITTED FOR TESTING")) {
                return "redirect:/board/tester";
            } else {
                String imagePath="";
                // normalize the file path
                String fileName = StringUtils.cleanPath(file.getOriginalFilename());
                String fileContentType=file.getContentType();
                String type=fileContentType.substring(0, fileContentType.indexOf("/"));
                if (bindingResult.hasErrors()||!type.equals("image")) {
                    List<Bug> allBugs = bugService.findBugsByTicketId(ticket);
                    model.addAttribute("ticket", ticket);
                    model.addAttribute("allBugs", allBugs);
                    model.addAttribute("imageError","only images allowed, check your file");
                    return "tickets/ticket-details-tester";
                } else {
                    Employee tester = employeeService.findByEmail(principal.getName());

                    // save the file on the local file system
                    try {
                        Path path = Paths.get(UPLOAD_DIR + fileName);
                        Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
                        imagePath="/images/"+fileName;
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    bug.setEmployeeId(tester);
                    bug.setFixed(false);
                    bug.setImagePath(imagePath);
                    bug.setTicketId(ticket);
                    bugService.save(bug);
                    redirectAttributes.addFlashAttribute("bugSaved",true);
                    return "redirect:/board/tester/projects/{projectId}/tickets/{ticketId}";
                }
            }
        } else return "redirect:/board/tester";

    }


}

