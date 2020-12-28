package com.example.HealthcareConnect.config;

import com.example.HealthcareConnect.datasource.Role;
import com.example.HealthcareConnect.datasource.User;
import com.example.HealthcareConnect.repository.RoleRepository;
import com.example.HealthcareConnect.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CustomUserDetailService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email);

        if (user == null) {
            throw new UsernameNotFoundException("Not found!");
        }

        List<String> roleStringList=new ArrayList<>();
        List<Role> roles = roleRepository.findAllByUserId(user.getId());

        if(roles.isEmpty()){
            roleStringList.add("USER");
        }else{
            for(Role role:roles){
                roleStringList.add(role.getRole());
            }
        }


        return org.springframework.security.core.userdetails.User
                .withUsername(user.getEmail())
                .username(user.getEmail())
                .password(user.getPassword())
                .roles(roleStringList.stream().collect(Collectors.joining()))
                .build();


    }
}
