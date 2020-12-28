package com.example.HealthcareConnect.service;

import com.example.HealthcareConnect.datasource.DocUser;
import com.example.HealthcareConnect.datasource.Role;
import com.example.HealthcareConnect.datasource.User;

import com.example.HealthcareConnect.exceptions.UserAlreadyExistsException;
import com.example.HealthcareConnect.exceptions.UserNotFoundException;
import com.example.HealthcareConnect.repository.DocRepository;
import com.example.HealthcareConnect.repository.RoleRepository;
import com.example.HealthcareConnect.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;


import java.io.IOException;
import java.util.List;

@Service
public class DocService {
    @Autowired
    private DocRepository docRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private UserRepository userRepository;

//    public DocUser createDoctor(DocUser docUser) {
//        return docRepository.save(docUser);
//    }
//    a doctor can't be created directly, only by promoting a regular user

    public List<DocUser> findAllDoctors() {
        return docRepository.findAll();
    }

    public void deleteDoctor(Integer id) {
        DocUser doc = docRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("This user is not a doctor"));

        roleRepository.deleteByUserId(doc.getUserId()); //o sa sterg din role si o sa se puna iar automat ca un user
        //apoi sterg si din lista de doctori
        docRepository.deleteById(id);
    }

    public DocUser update(Integer id, DocUser docUser) {
        DocUser oldDoc = docRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("This user is not a doctor or user does not exist!"));
        oldDoc.setStreet(docUser.getStreet());
        oldDoc.setCity(docUser.getCity());
        oldDoc.setLocation(docUser.getLocation());
        oldDoc.setDescription(docUser.getDescription());
//        oldDoc.setImage(docUser.getImage());
        oldDoc.setSpecialization(docUser.getSpecialization());
        //review can't be edited by doctor

        return docRepository.save(oldDoc);
    }

    public User promoteToDoctor(Integer id, DocUser docUser
//            , MultipartFile multipartFile) throws IOException {
    ) {
        User dbUser = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found!"));


        Role roleField = new Role();
        roleField.setUserId(id);
        roleField.setRole("DOCTOR");
        roleRepository.save(roleField);


//        String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
//        docUser.setImagePath(fileName);
//        String uploadDir="user-photo/" + docUser.getId();
//        FileUploadUtil.saveFile(uploadDir, fileName, multipartFile);

        docUser.setUserId(id);
        docRepository.save(docUser);

        return dbUser;
    }

//    private boolean alreadyExists(Integer id) {
//        Role role = roleRepository.findByUserId(id);
//        if (role != null) {
//            if (role.getRole().equalsIgnoreCase("DOCTOR")) {
//                throw new UserAlreadyExistsException("This account already has a doctor role!");
//            }
//        }
//        return false;
//    }


}


