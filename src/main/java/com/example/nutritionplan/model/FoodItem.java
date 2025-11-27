package com.example.nutritionplan.model;

import java.util.List;
import jakarta.persistence.*;

@Entity
@Table(name = "food_items")
public class FoodItem {
    private Integer food_id;
    private String name;
    private Double calories;
    private Double protein_g;
    private Double carbs_g;
    private Double fat_g;
    private List<String> categories;

    public FoodItem() {
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Integer getFood_id() {
        return food_id;
    }

    public void setFood_id(Integer food_id) {
        this.food_id = food_id;
    }

    @Column(nullable = false, unique = true)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Column(nullable = false)
    public Double getCalories() {
        return calories;
    }

    public void setCalories(Double calories) {
        this.calories = calories;
    }

    public Double getProtein_g() {
        return protein_g;
    }

    public void setProtein_g(Double protein_g) {
        this.protein_g = protein_g;
    }

    public Double getCarbs_g() {
        return carbs_g;
    }

    public void setCarbs_g(Double carbs_g) {
        this.carbs_g = carbs_g;
    }

    public Double getFat_g() {
        return fat_g;
    }

    public void setFat_g(Double fat_g) {
        this.fat_g = fat_g;
    }

    public List<String> getCategories() {
        return categories;
    }

    public void setCategories(List<String> categories) {
        this.categories = categories;
    }
}