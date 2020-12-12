package com.example.HealthcareConnect.repository;

import com.example.HealthcareConnect.datasource.PasswordRecovery;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PasswordRecoveryRepository extends JpaRepository<PasswordRecovery, Integer> {
    PasswordRecovery findByUuid(String uid);
}
