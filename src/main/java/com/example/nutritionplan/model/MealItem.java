package com.example.nutritionplan.model;

public class MealItem {
    private Integer meal_item_id;
    private Integer meal_id;
    private Integer food_id;
    private Double quantity_g;

    // Конструктор, геттери та сеттери
    public MealItem() {}

    public Integer getMeal_item_id() { return meal_item_id; }
    public void setMeal_item_id(Integer meal_item_id) { this.meal_item_id = meal_item_id; }
    public Integer getMeal_id() { return meal_id; }
    public void setMeal_id(Integer meal_id) { this.meal_id = meal_id; }
    public Integer getFood_id() { return food_id; }
    public void setFood_id(Integer food_id) { this.food_id = food_id; }
    public Double getQuantity_g() { return quantity_g; }
    public void setQuantity_g(Double quantity_g) { this.quantity_g = quantity_g; }
}