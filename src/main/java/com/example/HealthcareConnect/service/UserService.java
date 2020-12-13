package com.example.HealthcareConnect.service;

import com.example.HealthcareConnect.datasource.PasswordRecovery;
import com.example.HealthcareConnect.datasource.Role;
import com.example.HealthcareConnect.datasource.User;
import com.example.HealthcareConnect.dto.ResetPassword;
import com.example.HealthcareConnect.exceptions.FieldIsMandatory;
import com.example.HealthcareConnect.exceptions.UserNotFoundException;

import com.example.HealthcareConnect.repository.PasswordRecoveryRepository;
import com.example.HealthcareConnect.repository.RoleRepository;
import com.example.HealthcareConnect.repository.UserRepository;


import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
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



    public String forgetPassword(String email){
        User user= userRepository.findByEmail(email);
        if(user == null){
            throw new UserNotFoundException("This email is not valid!");
        }

        String uuid= UUID.randomUUID().toString();
        PasswordRecovery passwordRecovery = new PasswordRecovery();
        passwordRecovery.setUserId(user.getId());
        passwordRecovery.setDate(new Date());
        passwordRecovery.setUuid(uuid);

        passwordRecoveryRepository.save(passwordRecovery);
        return uuid;
    }

    public void resetPassword(ResetPassword resetPassword){
        PasswordRecovery passwordRecovery =
                passwordRecoveryRepository.findByUuid(resetPassword.getUid());

        if (passwordRecovery == null) {
            throw new RuntimeException(("Bad request"));
        }


        User user = userRepository.findById(passwordRecovery.getUserId()).get();

        if (user == null) {
            throw new RuntimeException("Bad request");
        }

//        user.setPassword(bCryptPasswordEncoder.encode(resetPassword.getNewPassword()));
        userRepository.save(user);
        passwordRecoveryRepository.delete(passwordRecovery);
    }


}
