package com.example.HealthcareConnect.ui;

import com.example.HealthcareConnect.datasource.DocUser;
import com.example.HealthcareConnect.exceptions.UserNotFoundException;
import com.example.HealthcareConnect.service.DocService;
import com.example.HealthcareConnect.service.PasswordService;
import com.example.HealthcareConnect.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.print.Doc;

@Controller
public class DocController {

    @Autowired
    private UserService userService;

    @Autowired
    private PasswordService passwordService;

    @Autowired
    private DocService docService;


    @GetMapping("/registerDoctors")
       public String registerDoctors(Model model){
        DocUser doc=new DocUser();
        model.addAttribute("doc", doc);
        return "registerDoctors";
    }
//    you need to be loged-in
    @RequestMapping(value="/registerDoctorsAction", method= RequestMethod.POST)
    public String registerDoctorsAction(@ModelAttribute DocUser doc){
        Integer currentUserId=userService.getCurrentUserId();
        if(currentUserId==null){
            return "redirect:login";
        }
        try {
            userService.promoteToDoctor(currentUserId, doc);
        }catch (Exception e){
            e.printStackTrace();
            return "registrationFailed";
        }

        return "home";
    }
}
