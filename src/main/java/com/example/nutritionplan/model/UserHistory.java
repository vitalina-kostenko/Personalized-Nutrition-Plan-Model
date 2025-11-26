package com.example.nutritionplan.model;

import java.util.ArrayList;
import java.util.List;

public class UserHistory {
    private UserProfile userProfile;
    private List<DailyLog> dailyLogs = new ArrayList<>();

    public UserHistory() {}

    public UserProfile getUserProfile() { return userProfile; }
    public void setUserProfile(UserProfile userProfile) { this.userProfile = userProfile; }
    public List<DailyLog> getDailyLogs() { return dailyLogs; }
    public void setDailyLogs(List<DailyLog> dailyLogs) { this.dailyLogs = dailyLogs; }
}