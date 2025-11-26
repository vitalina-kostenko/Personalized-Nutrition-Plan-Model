package com.example.nutritionplan.model;

import java.util.ArrayList;
import java.util.List;

public class UserSession {
    private UserProfile userProfile;
    private List<MealEntry> mealEntries = new ArrayList<>();
    private List<MealItem> mealItems = new ArrayList<>();

    public UserSession() {}

    public UserProfile getUserProfile() { return userProfile; }
    public void setUserProfile(UserProfile userProfile) { this.userProfile = userProfile; }
    public List<MealEntry> getMealEntries() { return mealEntries; }
    public void setMealEntries(List<MealEntry> mealEntries) { this.mealEntries = mealEntries; }
    public List<MealItem> getMealItems() { return mealItems; }
    public void setMealItems(List<MealItem> mealItems) { this.mealItems = mealItems; }
}