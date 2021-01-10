package com.example.HealthcareConnect.repository;

import com.example.HealthcareConnect.datasource.DocUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DocRepository extends JpaRepository<DocUser, Integer> {
    DocUser findByUserId(Integer id);
    void deleteByUserId(Integer userId);
//    List<DocUser> findByCityAndSpecialization(String city, String specialization);
    List<DocUser> findByCity(String city);
    List<DocUser> findBySpecialization(String specialization);
    List<DocUser> findByFirstLastName(String value);
}
