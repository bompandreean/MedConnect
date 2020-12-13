package com.example.HealthcareConnect.ui;

import com.example.HealthcareConnect.datasource.User;
import com.example.HealthcareConnect.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;

import java.util.List;

@Controller
public class MainController {

    @Autowired
    private UserService userService;

    @GetMapping("/")
    public String homePage(Model model){
//        User user=new User();
//
//        model.addAttribute("currentUser", user);
        return "home";
    }


    @GetMapping("/registration")
    public String showRegistrationForm( Model model) {
        User user=new User();
        model.addAttribute("user", user);
        return "registration";
    }

    @RequestMapping(value = "/createUser", method = RequestMethod.POST)
    public String registerUser(@ModelAttribute User user, BindingResult bindingResult){
        try {
            userService.create(user);

        }catch(Exception ex){
            ex.printStackTrace();
            System.out.println("Unable to create user");
//            model.addAttribute("errors", new String[]{"Unable to create user"});
//            return "registration";
        }
        return "login";
    }



    @GetMapping("/login")
    public String showLoginForm(Model model) {
        return "login";
    }
}
