package com.example.HealthcareConnect.controller;

import com.example.HealthcareConnect.datasource.*;
import com.example.HealthcareConnect.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.GetMapping;


@Controller
public class MainController {

    @Autowired
    private UserService userService;

    @Autowired
    private DocService docService;

    @Autowired
    private PasswordService passwordService;

    @Autowired
    private EmailService emailService;

    @Autowired
    private AppointmentService appointmentService;


    @GetMapping("/")
    public String homePage(Model model){
        model.addAttribute("currentUser", userService.getCurrentUsersDetails());
        return "home";
    }
    @GetMapping(value="/home")
    public  String home(Model model){
        model.addAttribute("currentUser", userService.getCurrentUsersDetails());
        return "home";}
    


    @GetMapping("/registration")
    public String showRegistrationForm( Model model) {
        User user=new User();
        model.addAttribute("user", user);
        return "registration";
    }

    @RequestMapping(value = "/createUser", method = RequestMethod.POST)
    public String registerUser(@ModelAttribute User user){
        try {
            userService.create(user);

        }catch(Exception ex){
            ex.printStackTrace();
            return "registrationFailed";
        }
        return "login";
    }



    @GetMapping("/login")
    public String showLoginForm(Model model) {
        return "login";
    }
    
    @GetMapping("/forgetPassword")
    public String forgetPassword(Model model){
        TemporaryUser tempUser=new TemporaryUser();
        model.addAttribute("tempUser", tempUser);
        return "forgetPassword";
    }


    @RequestMapping(value="/forgetPasswordAction", method=RequestMethod.POST)
    private String forgetPasswordAction(@ModelAttribute TemporaryUser temporaryUser,
                                        Model model) {
        passwordService.createTemporaryUser(temporaryUser);
        try {
           String code= passwordService.forgetPassword(temporaryUser.getEmail());
            String subject="Password Recovery";
            String text="Use the code below to change your password: "+code;
           emailService.sendSimpleMessage(temporaryUser.getEmail(),subject,text);
        }catch (Exception e){
            e.printStackTrace();
            return "failedPasswordRecovery";
        }
        model.addAttribute("temporaryPassword", new TemporaryPassword());
        return "resetPassword";
    }



    @RequestMapping(value="/resetPasswordAction", method=RequestMethod.POST)
    private String resetPasswordAction(@ModelAttribute TemporaryPassword temporaryPassword) {
        passwordService.createTemporaryPassword(temporaryPassword);
        try {
            passwordService.resetPassword(temporaryPassword);
        }catch (Exception e){
            e.printStackTrace();
            return "failedPasswordRecovery";
        }
        passwordService.deleteTemporaryPassword();
        passwordService.deleteTemporaryUser();
        return "login";
    }

    @RequestMapping(value="/profile", method =RequestMethod.GET)
    private String seeProfile(Model model){
        model.addAttribute("currentUser", userService.getCurrentUsersDetails());
        model.addAttribute("currentDocUser",
                docService.findByCurrentUserId(userService.getCurrentUserId()) );
        return "profile";
    }

//    @GetMapping(value="/deleteMyAccount")
//    private String deletePage(Model model){
//        model.addAttribute("currentUser", userService.getCurrentUsersDetails());
//        return "deletePage";
//    }
//
//    //TODO make a method to delete an existing account
//    @GetMapping(value="/deleteMyAccountAction")
//    private String deleteMyAccount(){
//        userService.deleteUserData(userService.getCurrentUserId());
//        return "redirect:/";
//    }


}
