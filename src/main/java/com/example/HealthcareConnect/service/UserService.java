package com.example.HealthcareConnect.service;

import com.example.HealthcareConnect.datasource.DocUser;
import com.example.HealthcareConnect.datasource.Role;
import com.example.HealthcareConnect.datasource.User;
import com.example.HealthcareConnect.exceptions.FieldIsMandatory;
import com.example.HealthcareConnect.exceptions.UserNotFoundException;

import com.example.HealthcareConnect.repository.*;


import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordRecoveryRepository passwordRecoveryRepository;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private EmailService emailService;

    @Autowired
    private TemporaryUserRepository temporaryUserRepository;

    @Autowired
    private DocRepository docRepository;

    public User create(User user) {
        validate(user);
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));

        return userRepository.save(user);
    }

    public void validate(User user) {
        if (user.getFirstName().isEmpty()) {
            throw new FieldIsMandatory("First Name is mandatory ");
        }
        if (user.getLastName().isEmpty()) {
            throw new FieldIsMandatory("Last Name is mandatory ");
        }
        if (user.getPassword().isEmpty()) {
            throw new FieldIsMandatory("Password number is mandatory ");
        }
        if (user.getEmail().isEmpty()) {
            throw new FieldIsMandatory("Email is mandatory ");
        }

        long count = userRepository.countByEmail(user.getEmail());
        if (count > 0) {
            throw new RuntimeException("Email already exists!");
        }
    }


    public User update(Integer id, User user) {
        // pacientii sau doctorii sa poata faca update doar la pagina lor

        User oldUser = userRepository
                .findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found"));
        oldUser.setFirstName(user.getFirstName());
        oldUser.setLastName(user.getLastName());

        oldUser.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
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


    public User promoteToAdmin(Integer id) {
        User dbUser = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found!"));

        Role roleField = new Role();
        roleField.setUserId(id);
        roleField.setRole("ADMIN");
        roleRepository.save(roleField);
        return dbUser;
    }



    public User getCurrentUsersDetails(){
        UserDetails userDetails=(UserDetails) SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal();

        String username=userDetails.getUsername();
        User user= userRepository.findByEmail(username);
        if(user==null){
            throw new UserNotFoundException("");
        }
        return user;
    }

    public Integer getCurrentUserId(){
        UserDetails principal = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Integer id= userRepository.findByEmail(principal.getUsername()).getId();
        return id;
    }



}
