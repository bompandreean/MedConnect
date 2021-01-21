package com.example.HealthcareConnect.controller;

import com.example.HealthcareConnect.datasource.DocUser;
import com.example.HealthcareConnect.service.DocService;
import com.example.HealthcareConnect.service.PasswordService;
import com.example.HealthcareConnect.service.UserService;


import com.example.HealthcareConnect.storage.FileUploadController;
import com.example.HealthcareConnect.storage.StorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


@Controller
public class DocController {

    private final StorageService storageService;

    @Autowired
    public DocController(StorageService storageService) {
        this.storageService = storageService;
    }

    @Autowired
    private UserService userService;

    @Autowired
    private PasswordService passwordService;

    @Autowired
    private DocService docService;




    @GetMapping("/registerDoctors")
    public String registerDoctors(Model model) {
        DocUser doc = new DocUser();
        model.addAttribute("doc", doc);
        return "registerDoctors";
    }
//    you need to be loged-in
//    @RequestMapping(value="/registerDoctorsAction", method= RequestMethod.POST)
//    public String registerDoctorsAction(@ModelAttribute DocUser doc,
//                                        @RequestParam("image") MultipartFile multipartFile)  {
//
//        Integer currentUserId=userService.getCurrentUserId();
//        if(currentUserId==null){
//            return "redirect:login";
//        }
//        try {
//            String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
//            doc.setImagePath(fileName);
//            String uploadDir="user-photo/" + doc.getId();
//            FileUploadUtil.saveFile(uploadDir, fileName, multipartFile);
//
//            docService.promoteToDoctor(currentUserId, doc, multipartFile);
//        }catch (Exception e) {
//            e.printStackTrace();
//            return "registrationFailed";
//        }
//
//        return "home";
//    }

    @RequestMapping(value = "/registerDoctorsAction", method = RequestMethod.POST)
    public String registerDoctorsAction(@ModelAttribute DocUser doc) {

        Integer currentUserId = userService.getCurrentUserId();
        if (currentUserId == null) {
            return "redirect:login";
        }
        try {
            docService.promoteToDoctor(currentUserId, doc);
        } catch (Exception e) {
            e.printStackTrace();
            return "registrationFailed";
        }

        return "home";
    }

    @RequestMapping(value = "/findDoctorsAction", method = RequestMethod.POST)
    public String findDoctorsAction(Model model,
                                    @Param("searchedVar") String searchedVar) {
//        System.out.println(searchedVar);
        List<DocUser> doctors = docService.findByCityOrSpecialization(searchedVar);
        model.addAttribute("doctors", doctors);
        model.addAttribute("currentUser", userService.getCurrentUsersDetails());
        return "seeDoctors";
    }

    @GetMapping("/allDoctors")
    public String getAllDoctors(Model model) {
        List<DocUser> doctors = docService.findAllDoctors();
        model.addAttribute("doctors", doctors);
        model.addAttribute("currentUser", userService.getCurrentUsersDetails());
        return "seeDoctors";
    }

    @GetMapping(value = "/findDoctors")
    public String findDoctors(Model model) {
        model.addAttribute("currentUser", userService.getCurrentUsersDetails());
        return "seeDoctors";
    }

    @GetMapping("/updateProfile")
    public String updateProfile(Model model) {

        model.addAttribute("files", storageService.loadAll().map(
                path -> MvcUriComponentsBuilder.fromMethodName(FileUploadController.class,
                        "serveFile", path.getFileName().toString()).build().toUri().toString())
                .collect(Collectors.toList()));
        model.addAttribute("currentDocUser",
                docService.findByCurrentUserId(userService.getCurrentUserId()) );
        return "updateProfile";
    }

    @RequestMapping(value = "/updateProfileAction", method = RequestMethod.POST)
    public String updateProfileAction(@ModelAttribute DocUser currentDocUser,
                                      @RequestParam("file") MultipartFile file,
                                      RedirectAttributes redirectAttributes){
        if(file.isEmpty()){
            Integer docUserId=currentDocUser.getId();
            currentDocUser.setImagePath(docService.getOldImagePath(docUserId));
            docService.update(currentDocUser.getId(), currentDocUser);
            return "redirect:/profile";
        }
        storageService.store(file);
        redirectAttributes.addFlashAttribute("message",
                "You successfully uploaded " + file.getOriginalFilename() + "!");

        currentDocUser.setImagePath("/files/"+file.getOriginalFilename());
        docService.update(currentDocUser.getId(), currentDocUser);
        return "redirect:/profile";
    }
//    @GetMapping("/appointment")
//    public String makeAppointment(Model model) {
//        model.addAttribute("currentDocUser",
//                docService.findByCurrentUserId(userService.getCurrentUserId()) );
//        return "makeAppointment";
//    }



}
