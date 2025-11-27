package com.example.nutritionplan.model;

import com.example.nutritionplan.model.user.UserProfile;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class UserProfileTest {

    @Test
    void testUserProfileCreationAndGetters() {
        Integer userId = 1;
        String firstName = "Іван";
        String dateOfBirth = "1990-08-27";
        String gender = "male";
        double height = 180.5;
        double weight = 85.2;
        String activityLevel = "moderate";
        String goal = "maintain";

        UserProfile profile = new UserProfile(userId, firstName, dateOfBirth, gender, height, weight, activityLevel, goal);

        assertNotNull(profile);
        assertEquals(userId, profile.getUser_id());
        assertEquals(firstName, profile.getFirst_name());
        assertEquals(dateOfBirth, profile.getDate_of_birth());
        assertEquals(gender, profile.getGender());
        assertEquals(height, profile.getHeight_cm());
        assertEquals(weight, profile.getWeight_kg());
        assertEquals(activityLevel, profile.getActivity_level());
        assertEquals(goal, profile.getGoal());
    }
}