package com.example.nutritionplan.model.repository;

import com.example.nutritionplan.model.FoodItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FoodItemRepository extends JpaRepository<FoodItem, Integer> {

    @Query(value = "SELECT * FROM food_items WHERE :category = ANY(categories)", nativeQuery = true)
    List<FoodItem> findByCategoriesContaining(@Param("category") String category);

}