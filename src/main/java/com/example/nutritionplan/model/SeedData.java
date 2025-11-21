package com.example.nutritionplan.model;

import java.util.List;

public class SeedData {
    private List<FoodItem> food_items;
    private List<User> users;
    private List<UserProfile> user_profiles;
    private List<MealEntry> meal_entries;
    private List<MealItem> meal_items;

    // Конструктор без аргументів (обов'язковий для JSON-маперів)
    public SeedData() {
    }

    // Getters and Setters
    public List<FoodItem> getFood_items() {
        return food_items;
    }

    public void setFood_items(List<FoodItem> food_items) {
        this.food_items = food_items;
    }

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }

    public List<UserProfile> getUser_profiles() {
        return user_profiles;
    }

    public void setUser_profiles(List<UserProfile> user_profiles) {
        this.user_profiles = user_profiles;
    }

    public List<MealEntry> getMeal_entries() {
        return meal_entries;
    }

    public void setMeal_entries(List<MealEntry> meal_entries) {
        this.meal_entries = meal_entries;
    }

    public List<MealItem> getMeal_items() {
        return meal_items;
    }

    public void setMeal_items(List<MealItem> meal_items) {
        this.meal_items = meal_items;
    }
}