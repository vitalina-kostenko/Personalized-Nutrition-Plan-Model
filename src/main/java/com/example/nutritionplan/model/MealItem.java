package com.example.nutritionplan.model;

import jakarta.persistence.*;

@Entity
@Table(name = "meal_items")
public class MealItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer meal_item_id;

    @Column(nullable = false)
    private Integer meal_id;

    @Column(nullable = false)
    private Integer food_id;

    @Column(nullable = false)
    private Double quantity_g;

    public MealItem() {

    }

    public MealItem(Integer meal_item_id, Integer meal_id, Integer food_id, Double quantity_g) {
        this.meal_item_id = meal_item_id;
        this.meal_id = meal_id;
        this.food_id = food_id;
        this.quantity_g = quantity_g;
    }

    public Integer getMeal_item_id() { return meal_item_id; }
    public void setMeal_item_id(Integer meal_item_id) { this.meal_item_id = meal_item_id; }
    public Integer getMeal_id() { return meal_id; }
    public void setMeal_id(Integer meal_id) { this.meal_id = meal_id; }
    public Integer getFood_id() { return food_id; }
    public void setFood_id(Integer food_id) { this.food_id = food_id; }
    public Double getQuantity_g() { return quantity_g; }
    public void setQuantity_g(Double quantity_g) { this.quantity_g = quantity_g; }
}