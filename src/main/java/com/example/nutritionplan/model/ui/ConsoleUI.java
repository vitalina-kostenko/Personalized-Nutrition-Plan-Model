package com.example.nutritionplan.model.ui;

import com.example.nutritionplan.model.FoodItem;
import com.example.nutritionplan.model.UserProfile;
import com.example.nutritionplan.model.service.FileStorageService;
import com.example.nutritionplan.model.service.NutritionCalculator;
import com.example.nutritionplan.model.shell.*; // Імпортуємо всі наші класи shell
import java.util.List;
import java.util.Scanner;

public class ConsoleUI {

    private final Scanner scanner;

    public ConsoleUI() {
        this.scanner = new Scanner(System.in, "UTF-8");
    }

    // Головний метод, який викликається з Application
    public void start(List<UserProfile> userProfiles, List<FoodItem> foodDatabase, NutritionCalculator calculator, FileStorageService storage) {

        // --- 1. СТВОРЕННЯ БАЗОВИХ КОМАНД ---
        Return returnCommand = new Return();
        Exit exitCommand = new Exit();

        // --- 2. СТВОРЕННЯ КОНКРЕТНИХ КОМАНД ---
        // Ми створюємо екземпляри команд, передаючи їм всі необхідні залежності
        CreateProfileCommand createProfileCmd = new CreateProfileCommand(scanner, userProfiles, storage);
        SelectProfileCommand selectProfileCmd = new SelectProfileCommand(scanner, userProfiles, foodDatabase, calculator, storage);

        // --- 3. ЗБІРКА МЕНЮ ПРОФІЛІВ ---
        Menu profileMenu = new Menu("profiles", scanner);
        profileMenu.add(returnCommand);
        profileMenu.add(exitCommand);
        profileMenu.add(new Help("Меню для роботи з профілями. 'create', 'select', 'list', 'return'."));
        profileMenu.add(createProfileCmd);
        profileMenu.add(selectProfileCmd);
        // profileMenu.add(new ListProfilesCommand(userProfiles)); // Ще одна корисна команда, яку ви можете створити

        // --- 4. ЗБІРКА ГОЛОВНОГО МЕНЮ ---
        Menu mainMenu = new Menu("main", scanner);
        mainMenu.add(exitCommand);
        mainMenu.add(new Help("Головне меню. 'profiles' - робота з профілями, 'exit' - вихід."));
        mainMenu.add(profileMenu); // Додаємо меню профілів як підменю

        // --- 5. ЗАПУСК ---
        displayWelcomeMessage();
        mainMenu.execute();
        displayGoodbyeMessage();
    }

    // Старі методи, які можна залишити
    public void displayWelcomeMessage() {
        System.out.println("=====================================================");
        System.out.println("Вітаємо у вашому персональному помічнику з харчування!");
        System.out.println("=====================================================");
    }

    public void displayGoodbyeMessage() {
        System.out.println("\nДо зустрічі!");
    }

    public void closeScanner() {
        scanner.close();
    }
}