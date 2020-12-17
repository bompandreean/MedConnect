package com.example.HealthcareConnect.repository;

import com.example.HealthcareConnect.datasource.TemporaryPassword;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TemporaryPasswordRepository extends JpaRepository<TemporaryPassword, Integer> {
}
