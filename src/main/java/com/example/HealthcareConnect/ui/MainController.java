package com.example.HealthcareConnect.ui;

import com.example.HealthcareConnect.datasource.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class MainController {

    @GetMapping("/")
    public String homePage(Model model){
        User user=new User();

        model.addAttribute("currentUser", user);
        return "home";
    }

    @GetMapping("/registration")
    public String showRegistrationForm(Model model) {
        return "registration";
    }

    @GetMapping("/login")
    public String showLoginForm(Model model) {
        return "login";
    }
}
