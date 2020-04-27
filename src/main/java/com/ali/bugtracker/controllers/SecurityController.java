package com.ali.bugtracker.controllers;

import com.ali.bugtracker.entities.Employee;
import com.ali.bugtracker.repositories.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;
import java.security.Principal;

@Controller
public class SecurityController {

    @Autowired
    EmployeeRepository employeeRepo;

    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;


    @GetMapping("/login")
    public  String displaySignIn(Principal principal){
        if(principal!=null)
            return "redirect:/";
        else
            return "/main/sign-in";
    }

    @GetMapping("/register")
    public String displayRegisterPage(Model model,Principal principal){
        if(principal!=null)
            return "redirect:/";
        else {
        Employee employee =new Employee();
        model.addAttribute("employee",employee);
        return "/main/register";
        }
    }

    @PostMapping("/register/save")
    public String saveEmployee(@Valid Employee employee,BindingResult bindingResult ,Model model){
        if (bindingResult.hasErrors()) {
            return "/main/register";
        }
        else {
            employee.setPassword(bCryptPasswordEncoder.encode(employee.getPassword()));
            employeeRepo.save(employee);
            return "redirect:/register?success";
        }
    }
}
