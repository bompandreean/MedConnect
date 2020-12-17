package com.example.HealthcareConnect.ui;

import com.example.HealthcareConnect.datasource.TemporaryPassword;
import com.example.HealthcareConnect.datasource.TemporaryUser;
import com.example.HealthcareConnect.datasource.User;
import com.example.HealthcareConnect.service.EmailService;
import com.example.HealthcareConnect.service.PasswordService;
import com.example.HealthcareConnect.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import org.springframework.web.bind.annotation.*;

@Controller
public class MainController {

    @Autowired
    private UserService userService;

    @Autowired
    private PasswordService passwordService;

    @Autowired
    private EmailService emailService;

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

//    @GetMapping("/resetPassword")
//    public String resetPassword(Model model){
//        TemporaryPassword temporaryPassword= new TemporaryPassword();
//        model.addAttribute("temporaryPassword", temporaryPassword);
//        return "resetPassword";
//    }

    @RequestMapping(value="/resetPasswordAction", method=RequestMethod.POST)
    private String resetPasswordAction(@ModelAttribute TemporaryPassword temporaryPassword) {
        passwordService.createTemporaryPassword(temporaryPassword);
        try {
            passwordService.resetPassword(temporaryPassword);
        }catch (Exception e){
            e.printStackTrace();
            return "failedPasswordRecovery";
        }
        return "login";
    }
}
