package com.example.HealthcareConnect.ui;

import com.example.HealthcareConnect.datasource.DocUser;
import com.example.HealthcareConnect.service.DocService;
import com.example.HealthcareConnect.service.PasswordService;
import com.example.HealthcareConnect.service.UserService;
import com.example.HealthcareConnect.storage.FileUploadUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Controller
public class DocController {

    @Autowired
    private UserService userService;

    @Autowired
    private PasswordService passwordService;

    @Autowired
    private DocService docService;
    private DocUser doc;
    private MultipartFile multipartFile;


    @GetMapping("/registerDoctors")
       public String registerDoctors(Model model){
        DocUser doc=new DocUser();
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

    @RequestMapping(value="/registerDoctorsAction", method= RequestMethod.POST)
    public String registerDoctorsAction(@ModelAttribute DocUser doc){

        Integer currentUserId=userService.getCurrentUserId();
        if(currentUserId==null){
            return "redirect:login";
        }
        try {
            docService.promoteToDoctor(currentUserId, doc);
        }catch (Exception e) {
            e.printStackTrace();
            return "registrationFailed";
        }

        return "home";
    }

    @GetMapping("/allDoctors")
    public String getAllDoctors(Model model){
        List<DocUser> doctors=docService.findAllDoctors();
        model.addAttribute("doctors", doctors);
        return "seeDoctors";
    }
}
