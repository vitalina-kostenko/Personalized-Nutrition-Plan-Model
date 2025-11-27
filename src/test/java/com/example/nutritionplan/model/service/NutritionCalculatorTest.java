package com.example.nutritionplan.model.service;

import com.example.nutritionplan.model.user.UserProfile;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class NutritionCalculatorTest {

    @Mock
    private AgeCalculationService ageServiceMock;

    @InjectMocks
    private NutritionCalculator nutritionCalculator;

    @Test
    void testCalculateDailyCalorieNeedsForFemale() {
        UserProfile profile = new UserProfile(
                2, "Віталіна", "2006-10-21", "female",
                170.0, 61.0, "sedentary", "lose_weight"
        );

        when(ageServiceMock.calculateAge("2006-10-21")).thenReturn(19);

        double actualCalories = nutritionCalculator.calculateDailyCalorieNeeds(profile);

        double expectedCalories = 1747;
        assertEquals(expectedCalories, Math.round(actualCalories));
    }
}