package com.example.nutritionplan.model.service;

import com.example.nutritionplan.model.UserProfile;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class NutritionCalculatorTest {

    private NutritionCalculator calculator;

    @BeforeEach
    void setUp() {
        calculator = new NutritionCalculator();
    }

    @Test
    void testCalculateDailyCalorieNeedsForMale() {
        UserProfile profile = new UserProfile(
                1,
                "Іван",
                "1990-11-21",
                "male",
                180.0,
                85.0,
                "moderate",
                "maintain"
        );

        double expectedCalories = 2933;
        double actualCalories = calculator.calculateDailyCalorieNeeds(profile);

        assertEquals(expectedCalories, Math.round(actualCalories));
    }

    @Test
    void testCalculateDailyCalorieNeedsForFemale() {
        UserProfile profile = new UserProfile(
                2,
                "Марія",
                "1995-11-21",
                "female",
                165.0,
                60.0,
                "light",
                "lose_weight"
        );

        double expectedCalories = 1903;
        double actualCalories = calculator.calculateDailyCalorieNeeds(profile);

        assertEquals(expectedCalories, Math.round(actualCalories));
    }

    @Test
    void testCalculateDailyCalorieNeedsThrowsExceptionForNullProfile() {
        assertThrows(IllegalArgumentException.class, () -> {
            calculator.calculateDailyCalorieNeeds(null);
        });
    }

    @Test
    void testCalculateDailyCalorieNeedsThrowsExceptionForInvalidGender() {
        UserProfile profile = new UserProfile(
                3,
                "Тест",
                "2000-01-01",
                "unknown",
                170.0,
                70.0,
                "sedentary",
                "maintain"
        );

        assertThrows(IllegalArgumentException.class, () -> {
            calculator.calculateDailyCalorieNeeds(profile);
        });
    }
}