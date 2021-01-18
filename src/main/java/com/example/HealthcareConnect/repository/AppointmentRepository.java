package com.example.HealthcareConnect.repository;

import com.example.HealthcareConnect.datasource.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Integer> {
    List<Appointment> findByUserId(Integer id);
    List<Appointment> findByDocId(Integer id);
}
