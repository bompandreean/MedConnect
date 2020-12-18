package com.example.HealthcareConnect.service;

import com.example.HealthcareConnect.datasource.PasswordRecovery;
import com.example.HealthcareConnect.datasource.TemporaryPassword;
import com.example.HealthcareConnect.datasource.TemporaryUser;
import com.example.HealthcareConnect.datasource.User;
import com.example.HealthcareConnect.exceptions.UserNotFoundException;
import com.example.HealthcareConnect.repository.PasswordRecoveryRepository;
import com.example.HealthcareConnect.repository.TemporaryPasswordRepository;
import com.example.HealthcareConnect.repository.TemporaryUserRepository;
import com.example.HealthcareConnect.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
public class PasswordService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordRecoveryRepository passwordRecoveryRepository;

    @Autowired
    private TemporaryUserRepository temporaryUserRepository;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private TemporaryPasswordRepository temporaryPasswordRepository;


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

    public void resetPassword(TemporaryPassword temporaryPassword){
        PasswordRecovery passwordRecovery =
                passwordRecoveryRepository.findByUuid(temporaryPassword.getUid());

        if (passwordRecovery == null) {
            throw new RuntimeException(("Bad request"));
        }


        User user = userRepository.findById(passwordRecovery.getUserId()).get();

        if (user == null) {
            throw new RuntimeException("Bad request");
        }

        user.setPassword(bCryptPasswordEncoder.encode(temporaryPassword.getNewPassword()));
        userRepository.save(user);
        passwordRecoveryRepository.delete(passwordRecovery);
    }

    public void createTemporaryUser(TemporaryUser temporaryUser){
        temporaryUserRepository.save(temporaryUser);
    }
    public void deleteTemporaryUser(){
        List<TemporaryUser> temporaryUserList= temporaryUserRepository.findAll();
        for(TemporaryUser tempUser:temporaryUserList){
        temporaryUserRepository.delete(tempUser);
        }

    }

    public void createTemporaryPassword(TemporaryPassword temporaryPassword){
        temporaryPasswordRepository.save(temporaryPassword);
    }
    public void deleteTemporaryPassword(){
        List<TemporaryPassword> temporaryPasswordList=temporaryPasswordRepository.findAll();
        for(TemporaryPassword tempPass: temporaryPasswordList){
            temporaryPasswordRepository.delete(tempPass);
        }

    }
}
