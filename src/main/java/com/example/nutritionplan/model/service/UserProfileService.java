package com.example.nutritionplan.model.service;

import com.example.nutritionplan.model.user.UserProfile;
import com.example.nutritionplan.model.repository.UserProfileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserProfileService {

    private final UserProfileRepository userProfileRepository;

    @Autowired
    public UserProfileService(UserProfileRepository userProfileRepository) {
        this.userProfileRepository = userProfileRepository;
    }

    public List<UserProfile> getAllProfiles() {
        return userProfileRepository.findAll();
    }

    public UserProfile createNewProfile(UserProfile userProfile) {
        return userProfileRepository.save(userProfile);
    }
}