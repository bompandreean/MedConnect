package com.example.HealthcareConnect.controller;

import com.example.HealthcareConnect.datasource.Appointment;
import com.example.HealthcareConnect.datasource.User;
import com.example.HealthcareConnect.service.AppointmentService;
import com.example.HealthcareConnect.service.DocService;
import com.example.HealthcareConnect.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller()
public class AppointmentsController {
    @Autowired
    private UserService userService;

    @Autowired
    private DocService docService;


    @Autowired
    private AppointmentService appointmentService;

    @GetMapping(value="/contact/{doc}")
    private String seeAppointmentPage(Model model,
                                      @PathVariable("doc") Integer doc){
        Appointment appointment=new Appointment();
        appointment.setDocId(doc);
        appointment.setUserId(userService.getCurrentUserId());
        model.addAttribute("appointment", appointment);
        model.addAttribute("currentUser", userService.getCurrentUsersDetails());
        return "appointment";
    }

    @PostMapping(value="/makeAppointment/{doc}")
    private String makeAppointment(@ModelAttribute Appointment appointment,
                                   @PathVariable("doc") Integer doc){
        appointment.setDocId(doc);
        appointment.setUserId(userService.getCurrentUserId());
        appointmentService.makeAppointment(appointment);
        return "redirect:/";
    }

    @GetMapping("/myAppointments")
    public String seeMyAppointments(Model model){
        List<Appointment> appointments=appointmentService
                .getMyAppointments(userService.getCurrentUserId());
        model.addAttribute("appointments", appointments);
//        model.addAttribute("doctors",
//                appointmentService.getMyDoctors(appointments));
        model.addAttribute("role", userService.getUserRole(userService.getCurrentUserId()));
        model.addAttribute("currentUser", userService.getCurrentUsersDetails());
        return "seeAppointments";
    }

    //if the user is a doctor, should be able to see the patient that requested the appointment
//    and change the status of the appointment
// if is a patient, should be able to see the doctors name?

//    @GetMapping("/appointmentDetails/{docId}")
//    public String appointmentDetails(@PathVariable("docId") Integer docId,
//                                     Model model){
//        try {
//            DocUser doc = docService.findById(docId);
//            model.addAttribute("doc", doc);
//        }catch (UsernameNotFoundException e){
//            e.getStackTrace();
////            return "redirect:errorPage";
//        }
//        return "";
//    }

    @GetMapping("/appointmentDetails/{appointmentId}")
    public String appointmentDetails(@PathVariable("appointmentId") Integer appId,
                                     Model model){
        model.addAttribute("currentUser", userService.getCurrentUsersDetails());
        try {
            Appointment appointment=appointmentService.findById(appId);
            User user= userService.findById(appointment.getUserId());
            model.addAttribute("user", user);
            model.addAttribute("appointment", appointment);

        }catch (UsernameNotFoundException e){
            e.getStackTrace();
//            return "redirect:errorPage";
        }
        return "appointmentDetails";
    }

    @PostMapping("/acceptAppointment/{appointmentId}")
    public String acceptApp(@PathVariable("appointmentId") Integer appId){
        try {
            appointmentService.changeState(appId, "ACCEPTED");
        }catch (UsernameNotFoundException e){
            e.getStackTrace();
        }
        return "redirect:/myAppointments";
    }

    @PostMapping("/declineAppointment/{appointmentId}")
    public String declineApp(@PathVariable("appointmentId") Integer appId){
        try {
            appointmentService.changeState(appId, "DENIED");
        }catch (UsernameNotFoundException e){
            e.getStackTrace();
        }
        return "redirect:/myAppointments";
    }
}
