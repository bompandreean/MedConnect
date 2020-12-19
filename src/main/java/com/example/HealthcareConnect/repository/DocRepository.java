package com.example.HealthcareConnect.repository;

import com.example.HealthcareConnect.datasource.DocUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DocRepository extends JpaRepository<DocUser, Integer> {
    DocUser findByUserId(Integer id);
}
