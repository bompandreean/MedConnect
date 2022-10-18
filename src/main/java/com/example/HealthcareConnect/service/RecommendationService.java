package com.example.HealthcareConnect.service;

import com.example.HealthcareConnect.datasource.Recommendation;
import com.example.HealthcareConnect.datasource.User;
import com.example.HealthcareConnect.exceptions.UserNotFoundException;
import com.example.HealthcareConnect.repository.RecommendationRepository;
import com.example.HealthcareConnect.repository.RoleRepository;
import com.example.HealthcareConnect.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@Service
public class RecommendationService {

    @Autowired
    private RecommendationRepository recommendationRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private UserRepository userRepository;

    public Recommendation saveRecommendation(Recommendation recom) {
        recom.setDateAndTime(LocalDateTime.now()
                .format(DateTimeFormatter.ofPattern("dd-MM-yy HH:mm")));
        return recommendationRepository.save(recom);
    }

    public Recommendation saveRecommendation(Recommendation recom, Integer giverId, Integer receiverId) {
        recom.setDateAndTime(LocalDateTime.now()
                .format(DateTimeFormatter.ofPattern("dd-MM-yy HH:mm")));
        recom.setGiverId(giverId);
        recom.setReceiverId(receiverId);
        return recommendationRepository.save(recom);
    }

    public List<Recommendation> getMyRecommendations(Integer id) {
        if (roleRepository.findByUserId(id).getRole().equalsIgnoreCase("DOCTOR")) {
            return compareMyRecommendations(recommendationRepository.findByGiverId(id));
        } //if it's a doctor -> it's a giver
        return compareMyRecommendations(recommendationRepository.findByReceiverId(id));
    }

    private List<Recommendation> compareMyRecommendations(List<Recommendation> recommendations) {
        Collections.sort(recommendations, new RecommendationComparator());
        return recommendations;
    }

    public User getUserByRecommendationId(Integer id, Integer currentUserId) {
        Integer receiver = recommendationRepository.findById(id).get().getReceiverId();
        Integer giver = recommendationRepository.findById(id).get().getGiverId();
        if (currentUserId == receiver) {
            return userRepository.findById(giver)
                    .orElseThrow(() -> new UserNotFoundException("User not found"));
        }
        return userRepository.findById(receiver)
                .orElseThrow(() -> new UserNotFoundException("User not found"));
    }

    public Recommendation findById(Integer id) {
        return recommendationRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("Recommendation not found"));
    }
}

class RecommendationComparator implements Comparator<Recommendation> {
    @Override
    public int compare(Recommendation o1, Recommendation o2) {
        return Integer.compare(o2.getId(), o1.getId());
        //ordine descrescatoare, cel mai nou deasupra
    }
}
