package com.example.nutritionplan.model.service;

import com.example.nutritionplan.model.UserProfile;

import java.time.LocalDate;
import java.time.Period;

public class NutritionCalculator {

    public double calculateDailyCalorieNeeds(UserProfile profile) {
        if (profile == null) {
            throw new IllegalArgumentException("Профіль користувача не може бути null");
        }

        int age = calculateAge(profile.getDate_of_birth());
        double bmr = calculateBmr(profile.getGender(), profile.getWeight_kg(), profile.getHeight_cm(), age);
        double activityMultiplier = getActivityMultiplier(profile.getActivity_level());

        return bmr * activityMultiplier;
    }

    private double calculateBmr(String gender, double weightKg, double heightCm, int age) {
        if ("male".equalsIgnoreCase(gender)) {
            return 88.362 + (13.397 * weightKg) + (4.799 * heightCm) - (5.677 * age);
        } else if ("female".equalsIgnoreCase(gender)) {
            return 447.593 + (9.247 * weightKg) + (3.098 * heightCm) - (4.330 * age);
        } else {
            throw new IllegalArgumentException("Невідома стать для розрахунку BMR: " + gender);
        }
    }

    private double getActivityMultiplier(String activityLevel) {
        if (activityLevel == null) {
            return 1.2;
        }

        switch (activityLevel.toLowerCase()) {
            case "sedentary":
                return 1.2;
            case "light":
                return 1.375;
            case "moderate":
                return 1.55;
            case "active":
                return 1.725;
            case "very_active":
                return 1.9;
            default:
                return 1.2;
        }
    }

    private int calculateAge(String dateOfBirth) {
        if (dateOfBirth == null || dateOfBirth.isEmpty()) {
            throw new IllegalArgumentException("Дата народження не може бути порожньою");
        }
        LocalDate birthDate = LocalDate.parse(dateOfBirth);
        LocalDate currentDate = LocalDate.now();
        return Period.between(birthDate, currentDate).getYears();
    }
}