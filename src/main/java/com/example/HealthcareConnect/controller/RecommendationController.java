package com.example.HealthcareConnect.controller;

import com.example.HealthcareConnect.datasource.Recommendation;
import com.example.HealthcareConnect.service.RecommendationService;
import com.example.HealthcareConnect.service.UserService;
import org.apache.logging.log4j.util.PropertySource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.Comparator;

@Controller
public class RecommendationController {
    @Autowired
    private RecommendationService recommendationService;

    @Autowired
    private UserService userService;

    @GetMapping("/recommendation/{patientId}")
    public String newRecommendationPage(@PathVariable(name="patientId") Integer id,
                                        Model model){
        Recommendation recommendation=new Recommendation();
        model.addAttribute("recommendation", recommendation);

        model.addAttribute("currentUserRole", userService.getUserRole(userService.getCurrentUserId()));
        model.addAttribute("currentUser", userService.getCurrentUserId());
        //for navbar
        return "recommendation";
    }

    @PostMapping("/recommendationAction/{patientId}")
    public String createRecommendationAction(@ModelAttribute Recommendation recommendation,
                                             @PathVariable(name="patientId") Integer id){

        recommendationService.saveRecommendation(recommendation, userService.getCurrentUserId(), id);
        return "redirect:/";
    }
    
    @GetMapping("/myRecommendations")
    public String seeMyRecommendations(Model model){
       model.addAttribute("recommendations", recommendationService
               .getMyRecommendations(userService.getCurrentUserId()));

        model.addAttribute("currentUser", userService.getCurrentUserId());
        //for navbar
       return "seeRecommendations";
    }

   @GetMapping("/recommendationDetails/{recomId}")
   public String recommendationDetails(@PathVariable(value = "recomId") Integer id,
                                       Model model){
        model.addAttribute("user", recommendationService
                .getUserByRecommendationId(id, userService.getCurrentUserId()));

        model.addAttribute("recommendation", recommendationService.findById(id));
        model.addAttribute("currentUser", userService.getCurrentUserId());
        return "recommendationDetails";
   }

}

