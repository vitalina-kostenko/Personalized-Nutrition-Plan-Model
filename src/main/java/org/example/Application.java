package org.example;

import com.example.nutritionplan.model.FoodItem;
import com.example.nutritionplan.model.UserProfile;
import com.example.nutritionplan.model.service.FileStorageService;
import com.example.nutritionplan.model.service.NutritionCalculator;
import com.example.nutritionplan.model.ui.ConsoleUI;
import java.util.List;

public class Application {
    private final ConsoleUI ui;
    private final FileStorageService storage;
    private final NutritionCalculator calculator;

    public Application(ConsoleUI ui, FileStorageService storage, NutritionCalculator calculator) {
        this.ui = ui;
        this.storage = storage;
        this.calculator = calculator;
    }

    public void run() {
        List<UserProfile> userProfiles = storage.loadUserProfiles();
        List<FoodItem> foodDatabase = storage.loadFoodDatabase();

        if (foodDatabase == null) {
            System.err.println("Критична помилка: не вдалося завантажити базу продуктів. Робота програми неможлива.");
            return;
        }

        ui.start(userProfiles, foodDatabase, calculator, storage);

//        storage.saveUserProfiles(userProfiles);

        System.out.println("Роботу завершено, дані збережено.");

        ui.closeScanner();
    }
}