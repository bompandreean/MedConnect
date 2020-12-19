package com.example.HealthcareConnect.service;

import com.example.HealthcareConnect.datasource.DocUser;
import com.example.HealthcareConnect.exceptions.UserNotFoundException;
import com.example.HealthcareConnect.repository.DocRepository;
import com.example.HealthcareConnect.repository.RoleRepository;
import com.example.HealthcareConnect.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
        oldDoc.setImagePath(docUser.getImagePath());
        oldDoc.setSpecialization(docUser.getSpecialization());
        //review can't be edited by doctor

         return  docRepository.save(oldDoc);
    }
}


