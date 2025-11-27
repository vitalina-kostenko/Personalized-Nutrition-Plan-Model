package com.example.nutritionplan.model.service;

import com.example.nutritionplan.model.UserProfile;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class NutritionCalculator {
    private static final Logger logger = LogManager.getLogger(NutritionCalculator.class);

    private final AgeCalculationService ageService;


    public NutritionCalculator(AgeCalculationService ageService) {
        this.ageService = ageService;
    }

    public double calculateDailyCalorieNeeds(UserProfile profile) {
        logger.info("Починаємо розрахунок калорій для користувача: {}", profile.getFirst_name());

        if (profile == null) {
            logger.error("Профіль користувача дорівнює null!");
            throw new IllegalArgumentException("Профіль користувача не може бути null");
        }

        int age = ageService.calculateAge(profile.getDate_of_birth());
        logger.debug("Розрахований вік: {} років", age);

        double bmr = calculateBmr(profile.getGender(), profile.getWeight_kg(), profile.getHeight_cm(), age);
        logger.debug("Розрахований BMR: {}", bmr);

        double activityMultiplier = getActivityMultiplier(profile.getActivity_level());
        logger.debug("Множник активності: {}", activityMultiplier);

        double result = bmr * activityMultiplier;
        logger.info("Завершено розрахунок. Рекомендована норма: {} ккал", Math.round(result));

        return result;
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
            case "sedentary": return 1.2;
            case "light": return 1.375;
            case "moderate": return 1.55;
            case "active": return 1.725;
            case "very_active": return 1.9;
            default: return 1.2;
        }
    }
}
