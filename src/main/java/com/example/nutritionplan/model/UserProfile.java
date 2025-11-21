package com.example.nutritionplan.model;

public class UserProfile {
    private Integer user_id;
    private String first_name;
    private String date_of_birth;
    private String gender;
    private Double height_cm;
    private Double weight_kg;
    private String activity_level;
    private String goal;

    public UserProfile() {
    }

    public UserProfile(Integer user_id, String first_name, String date_of_birth, String gender,
                       Double height_cm, Double weight_kg, String activity_level, String goal) {
        this.user_id = user_id;
        this.first_name = first_name;
        this.date_of_birth = date_of_birth;
        this.gender = gender;
        this.height_cm = height_cm;
        this.weight_kg = weight_kg;
        this.activity_level = activity_level;
        this.goal = goal;
    }

    public Integer getUser_id() {
        return user_id;
    }

    public void setUser_id(Integer user_id) {
        this.user_id = user_id;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getDate_of_birth() {
        return date_of_birth;
    }

    public void setDate_of_birth(String date_of_birth) {
        this.date_of_birth = date_of_birth;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public Double getHeight_cm() {
        return height_cm;
    }

    public void setHeight_cm(Double height_cm) {
        this.height_cm = height_cm;
    }

    public Double getWeight_kg() {
        return weight_kg;
    }

    public void setWeight_kg(Double weight_kg) {
        this.weight_kg = weight_kg;
    }

    public String getActivity_level() {
        return activity_level;
    }

    public void setActivity_level(String activity_level) {
        this.activity_level = activity_level;
    }

    public String getGoal() {
        return goal;
    }

    public void setGoal(String goal) {
        this.goal = goal;
    }
}