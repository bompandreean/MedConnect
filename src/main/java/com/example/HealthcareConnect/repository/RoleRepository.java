package com.example.HealthcareConnect.repository;

import com.example.HealthcareConnect.datasource.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoleRepository extends JpaRepository<Role, Integer> {
    List<Role> findByRole(String role);
    List<Role> findByUserId(Integer id);


}
