package com.example.HealthcareConnect.repository;

import com.example.HealthcareConnect.datasource.TemporaryUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TemporaryUserRepository extends JpaRepository<TemporaryUser, Integer> {

}
