package com.example.HealthcareConnect.service;

import com.example.HealthcareConnect.datasource.Role;
import com.example.HealthcareConnect.datasource.User;
import com.example.HealthcareConnect.exceptions.FieldIsMandatory;
import com.example.HealthcareConnect.exceptions.UserNotFoundException;

import com.example.HealthcareConnect.repository.RoleRepository;
import com.example.HealthcareConnect.repository.UserRepository;


import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    public User create(User user) {
        validate(user);

        long count = userRepository.countByEmail(user.getEmail());
        if (count > 0) {
            throw new RuntimeException("Email already exists!");
        }

        //encode password
        return userRepository.save(user);
    }

    public void validate(User user) {
        if (user.getFirstName().isEmpty()) {
            throw new FieldIsMandatory("First Name is mandatory ");
        }
        if (user.getLastName().isEmpty()) {
            throw new FieldIsMandatory("Last Name is mandatory ");
        }
        if (user.getPhone().isEmpty()) {
            throw new FieldIsMandatory("Phone number is mandatory ");
        }
        if (user.getEmail().isEmpty()) {
            throw new FieldIsMandatory("Email is mandatory ");
        }
    }


    public User update(Integer id, User user) {
        // pacientii sau doctorii sa poata faca update doar la pagina lor

        User oldUser = userRepository
                .findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found"));
        oldUser.setFirstName(user.getFirstName());
        oldUser.setLastName(user.getLastName());
        oldUser.setPhone(user.getPhone());
        return userRepository.save(oldUser);
    }

    public void delete(Integer id) {
        userRepository.deleteById(id);
    }


    //available only for admins
    public List<User> findAll() {
        return userRepository.findAll();
    }

    public List<User> findByRole(String role) {
        List<Role> roles;
        if (Strings.isEmpty(role)) {
            roles = roleRepository.findByRole("DOCTOR");
        } else {
            roles = roleRepository.findByRole(role);
        }
        if(roles.isEmpty()){
            throw new UserNotFoundException("Users with this role not found!");
        }
        List<User> users = new ArrayList<>();
        for (Role role1 : roles) {
            users.add(userRepository.findById(role1.getId()).get());
        }
        return users;

    }

    public String validateRole(String stringRole){
        if(stringRole.equalsIgnoreCase("doctor")) {
            return "DOCTOR";
        }
        if(stringRole.equalsIgnoreCase("patient")){
            return "PATIENT";
        }
        if(stringRole.equalsIgnoreCase("admin")){
            return "ADMIN";
        }
        throw new UserNotFoundException("This role is nonexistent!");
    }


    public User promote(Integer id, String role) {
        User dbUser = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found!"));

        Role roleField = new Role();
        roleField.setUserId(id);
        roleField.setRole(validateRole(role));
        roleRepository.save(roleField);
        return dbUser;
    }




}
