package com.ali.bugtracker.controllers;

import com.ali.bugtracker.entities.ConfirmationToken;
import com.ali.bugtracker.entities.Employee;
import com.ali.bugtracker.repositories.EmployeeRepository;
import com.ali.bugtracker.services.ConfirmationTokenService;
import com.ali.bugtracker.services.EmailSenderService;
import com.ali.bugtracker.services.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;

@Controller
public class SecurityController {

    @Autowired
    ConfirmationTokenService confirmationTokenService;
    @Autowired
    private EmailSenderService emailSenderService;

    @Autowired
    EmployeeService employeeService;

    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;


    @GetMapping("/login")
    public  String displaySignIn(Principal principal){
        if(principal!=null)
            return "redirect:/profile";
        else
            return "/main/sign-in";
    }

    @GetMapping("/register")
    public String displayRegisterPage(Model model,Principal principal){
        if(principal!=null)
            return "redirect:/profile";
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
            employeeService.save(employee);
            ConfirmationToken confirmationToken = new ConfirmationToken(employee);
            confirmationTokenService.save(confirmationToken);
            SimpleMailMessage mailMessage = new SimpleMailMessage();
            mailMessage.setTo(employee.getEmail());
            mailMessage.setSubject("Complete Registration In Bug Tracker!");
            mailMessage.setFrom("bugtrackeremailservice@gmail.com");
            mailMessage.setText("To confirm your account, please click here : "
                    +"http://localhost:8080/confirm-account?token="+confirmationToken.getConfirmationToken());

            emailSenderService.sendEmail(mailMessage);
            return "redirect:/register?success";
        }
    }


    @RequestMapping(value="/confirm-account", method= {RequestMethod.GET, RequestMethod.POST})
    public String confirmUserAccount(Model model, @RequestParam("token")String confirmationToken)
    {
        ConfirmationToken token = confirmationTokenService.findByConfirmationToken(confirmationToken);

        if(token != null)
        {
            Employee employee = employeeService.findByEmail(token.getEmployee().getEmail());
            employee.setEnabled(true);
            employeeService.save(employee);
            return"redirect:/login?success";
        }
        else
        {
            return"redirect:/register?error";

        }
    }
}
