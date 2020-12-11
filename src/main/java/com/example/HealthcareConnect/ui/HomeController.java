package com.example.HealthcareConnect.ui;

import com.example.HealthcareConnect.datasource.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping("/")
    public String homePage(Model model){
        User user=new User();

        model.addAttribute("currentUser", user);
        return "home";
    }
}
