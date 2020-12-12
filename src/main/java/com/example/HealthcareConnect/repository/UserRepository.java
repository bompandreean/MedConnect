package com.example.HealthcareConnect.repository;

import com.example.HealthcareConnect.datasource.User;
import org.springframework.data.domain.Example;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    long countByEmail(String email);
//    List<User> findByLastName(String name);
    User findByEmail(String email);
    List<User> findAll();

}
