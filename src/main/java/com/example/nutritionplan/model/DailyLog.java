package com.example.nutritionplan.model;

import java.util.ArrayList;
import java.util.List;

public class DailyLog {
    private String date;
    private List<MealEntry> mealEntries = new ArrayList<>();
    private List<MealItem> mealItems = new ArrayList<>();

    public DailyLog() {}

    public DailyLog(String date) {
        this.date = date;
    }

    public String getDate() { return date; }
    public void setDate(String date) { this.date = date; }
    public List<MealEntry> getMealEntries() { return mealEntries; }
    public void setMealEntries(List<MealEntry> mealEntries) { this.mealEntries = mealEntries; }
    public List<MealItem> getMealItems() { return mealItems; }
    public void setMealItems(List<MealItem> mealItems) { this.mealItems = mealItems; }
}