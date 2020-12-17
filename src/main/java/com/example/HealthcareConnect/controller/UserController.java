package com.example.HealthcareConnect.controller;

import com.example.HealthcareConnect.datasource.User;
import com.example.HealthcareConnect.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping
    private User create(@RequestBody User user) {
        return userService.create(user);
    }

    @GetMapping("/findAll")
    private List<User> getAll() {
        return userService.findAll();
    }

    @DeleteMapping("/delete/{id}")
    private void delete(@PathVariable(name = "id") Integer id) {
        userService.delete(id);
    }

    @PutMapping("/update/{id}")
    private User update(@PathVariable(name = "id") Integer id, @RequestBody User user) {
        return userService.update(id, user);
    }

    @GetMapping("/findByRole")
    private List<User> getByRole(@RequestParam(name = "role", required = false) String role) {
        return userService.findByRole(role);
    }

    @PutMapping("/promote/{id}")
    private User promote(@PathVariable(name = "id") Integer id,
                         @RequestParam(name = "role", required = true) String role) {
        return userService.promote(id, role);

    }

//    @PostMapping("/forgetPassword/{email}")
//    private void forgetPassword(@PathVariable(name = "email") String email) {
//        userService.forgetPassword(email);
//    }
//
//    @PostMapping("/resetPassword")
//    private void resetPassword(@RequestBody ResetPassword resetPassword) {
//        userService.resetPassword(resetPassword);
//    }
}
