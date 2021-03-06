package com.example.HealthcareConnect.service;

import com.example.HealthcareConnect.datasource.Appointment;
import com.example.HealthcareConnect.datasource.DocUser;
import com.example.HealthcareConnect.datasource.Role;
import com.example.HealthcareConnect.datasource.User;
import com.example.HealthcareConnect.exceptions.UserNotFoundException;
import com.example.HealthcareConnect.repository.AppointmentRepository;
import com.example.HealthcareConnect.repository.DocRepository;
import com.example.HealthcareConnect.repository.RoleRepository;
import com.example.HealthcareConnect.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDate;
import java.util.*;

@Service
public class AppointmentService {

    @Autowired
    private AppointmentRepository appointmentRepository;

//    @Autowired
//    private DocRepository docRepository;
//
//    @Autowired
//    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;


    public void makeAppointment(Appointment appointment){

        appointment.setAppStatus("Processing");
        appointment.setCurrentDay(LocalDate.now().toString());
        appointmentRepository.save(appointment);
    }

//    public Appointment updateAppointment(Integer userId, Integer docId, Appointment ap){
//        Appointment appointment=appointmentRepository.findById(ap.getId())
//                .orElseThrow(()->new EntityNotFoundException("not found"));
//        appointment.setUserId(userId);
//        appointment.setDocId(docId);
//        appointment.setAppointmentDate(ap.getAppointmentDate());
//        appointment.setAppointmentTime(ap.getAppointmentTime());
//        appointment.setBriefDescription(ap.getBriefDescription());
//        appointment.setPhone(ap.getPhone());
//        appointment.setAppStatus("Processing");
//
//        return appointmentRepository.save(appointment);
//    }

    public Appointment changeState(Integer id, String status){
        Appointment appointment=appointmentRepository.findById(id)
                .orElseThrow(()->new UserNotFoundException("Appointment not found!"));
        appointment.setAppStatus(status);

        return appointmentRepository.save(appointment);
    }

    public List<Appointment> getMyAppointments(Integer userId){
        Role role=roleRepository.findByUserId(userId);
        if(role.getRole().equalsIgnoreCase("DOCTOR")){
            return compareApp(appointmentRepository.findByDocId(userId));
        }
        return compareApp(appointmentRepository.findByUserId(userId));
    }

    public List<Appointment> compareApp(List<Appointment> app){
        Collections.sort(app, new AppointmentComparator());
        return app;
    }

//    public List<DocUser> getMyDoctors(List<Appointment> myApp){
//        List<DocUser> myDoctors=new ArrayList<>();
//        for(Appointment app:myApp){
//            myDoctors.add(docRepository.findById(app.getDocId()).get());
//        }
//        return myDoctors;
//    }
//
//
//    public List<User> getMyPatients(List<Appointment> myApp){
//        List<User> myPatients=new ArrayList<>();
//        for(Appointment app:myApp){
//            myPatients.add(userRepository.findById(app.getUserId()).get());
//        }
//        return myPatients;
//    }

    public Appointment findById(Integer id){
        return appointmentRepository.findById(id)
                .orElseThrow(()->new UserNotFoundException("Appointment not found!"));
    }
}
class AppointmentComparator implements Comparator<Appointment>{
    @Override
    public int compare(Appointment o1, Appointment o2) {
        return Integer.compare(o2.getId(), o1.getId());
    }
}
