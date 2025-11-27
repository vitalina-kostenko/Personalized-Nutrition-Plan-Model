package com.example.nutritionplan.model;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;

public class UserProfileBuilder {
    private UserProfile profile = new UserProfile();

    public UserProfileBuilder() {
        // Ініціалізація полів початковими даними за замовчуванням
        profile.setGoal("maintain");
        profile.setActivity_level("sedentary");
    }

    public static UserProfileBuilder create() {
        return new UserProfileBuilder();
    }

    public UserProfileBuilder userId(Integer id) {
        profile.setUser_id(id);
        return this;
    }

    public UserProfileBuilder firstName(String name) {
        profile.setFirst_name(name);
        return this;
    }

    public UserProfileBuilder dateOfBirth(String dob) {
        profile.setDate_of_birth(dob);
        return this;
    }

    public UserProfileBuilder gender(String gender) {
        profile.setGender(gender);
        return this;
    }

    public UserProfileBuilder height(double height) {
        profile.setHeight_cm(height);
        return this;
    }

    public UserProfileBuilder weight(double weight) {
        profile.setWeight_kg(weight);
        return this;
    }

    public UserProfileBuilder activityLevel(String level) {
        profile.setActivity_level(level);
        return this;
    }

    public UserProfileBuilder goal(String goal) {
        profile.setGoal(goal);
        return this;
    }

    public UserProfile build() {
        // Перевірка коректності даних перед поверненням об'єкта
        if (profile.getUser_id() == null || profile.getUser_id() <= 0) {
            throw new IllegalStateException("ID користувача не встановлено або є некоректним.");
        }
        if (profile.getFirst_name() == null || profile.getFirst_name().trim().isEmpty()) {
            throw new IllegalStateException("Ім'я користувача не може бути порожнім.");
        }
        if (profile.getWeight_kg() <= 0 || profile.getHeight_cm() <= 0) {
            throw new IllegalStateException("Вага та зріст повинні бути додатніми числами.");
        }
        try {
            LocalDate.parse(profile.getDate_of_birth());
        } catch (DateTimeParseException e) {
            throw new IllegalStateException("Неправильний формат дати народження. Очікується РРРР-ММ-ДД.");
        }

        return profile;
    }
}