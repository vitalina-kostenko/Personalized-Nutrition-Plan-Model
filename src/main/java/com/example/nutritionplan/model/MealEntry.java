package com.example.nutritionplan.model;
import jakarta.persistence.*;

@Entity
@Table(name = "meal_entries")
public class MealEntry {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer meal_id;

    @Column(nullable = false)
    private Integer user_id;

    @Column(nullable = false)
    private String meal_type;

    @Column(nullable = false)
    private String meal_date;

    public MealEntry() {
    }

    public MealEntry(Integer meal_id, Integer user_id, String meal_type, String meal_date) {
        this.meal_id = meal_id;
        this.user_id = user_id;
        this.meal_type = meal_type;
        this.meal_date = meal_date;
    }

    public Integer getMeal_id() { return meal_id; }
    public void setMeal_id(Integer meal_id) { this.meal_id = meal_id; }
    public Integer getUser_id() { return user_id; }
    public void setUser_id(Integer user_id) { this.user_id = user_id; }
    public String getMeal_type() { return meal_type; }
    public void setMeal_type(String meal_type) { this.meal_type = meal_type; }
    public String getMeal_date() { return meal_date; }
    public void setMeal_date(String meal_date) { this.meal_date = meal_date; }
}

