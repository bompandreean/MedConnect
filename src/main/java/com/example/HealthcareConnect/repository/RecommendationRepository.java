package com.example.HealthcareConnect.repository;

import com.example.HealthcareConnect.datasource.Recommendation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RecommendationRepository extends JpaRepository<Recommendation, Integer> {
    List<Recommendation> findByReceiverId(Integer receiverId);
    List<Recommendation> findByGiverId(Integer giverId);
}
