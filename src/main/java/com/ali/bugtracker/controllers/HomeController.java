package com.ali.bugtracker.controllers;


import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.security.Principal;

@Controller()
@RequestMapping("/")
public class HomeController {

    @GetMapping()
    public String displayHome(){
        return "main/index";
    }

    @GetMapping("dashboard")
    @ResponseBody
    public String dash(Principal principal){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String info =auth.getName()+"----" +auth.getDetails()+"----"+principal.getName();;

        return info;
    }
}
