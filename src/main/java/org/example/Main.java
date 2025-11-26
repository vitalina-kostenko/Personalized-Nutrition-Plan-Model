package org.example;

import com.example.nutritionplan.model.service.AgeCalculationService;
import com.example.nutritionplan.model.service.AgeCalculationServiceImpl;
import com.example.nutritionplan.model.service.FileStorageService;
import com.example.nutritionplan.model.service.NutritionCalculator;
import com.example.nutritionplan.model.ui.ConsoleUI;

public class Main {
    public static void main(String[] args) {
        ConsoleUI ui = new ConsoleUI();
        FileStorageService storage = new FileStorageService();
        AgeCalculationService ageService = new AgeCalculationServiceImpl();
        NutritionCalculator calculator = new NutritionCalculator(ageService);
        Application app = new Application(ui, storage, calculator);

        app.run();
    }
}