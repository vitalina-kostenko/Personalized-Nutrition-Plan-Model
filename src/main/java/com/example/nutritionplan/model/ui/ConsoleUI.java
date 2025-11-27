package com.example.nutritionplan.model.ui;

import com.example.nutritionplan.model.FoodItem;
import com.example.nutritionplan.model.user.UserProfile;
import com.example.nutritionplan.model.repository.FoodItemRepository;
import com.example.nutritionplan.model.repository.UserProfileRepository;
import com.example.nutritionplan.model.service.FileStorageService;
import com.example.nutritionplan.model.service.NutritionCalculator;
import com.example.nutritionplan.model.shell.*;

import java.util.List;
import java.util.Scanner;

import org.springframework.stereotype.Component;

@Component
public class ConsoleUI {

    private final Scanner scanner;

    public ConsoleUI(Scanner scanner) {
        this.scanner = scanner;
    }

    public void start(UserProfileRepository userProfileRepo,
                      FoodItemRepository foodItemRepo,
                      NutritionCalculator calculator,
                      FileStorageService fileStorageService) {

        Return returnCommand = new Return();
        Exit exitCommand = new Exit();

        CreateProfileCommand createProfileCmd = new CreateProfileCommand(scanner, userProfileRepo);

        SelectProfileCommand selectProfileCmd = new SelectProfileCommand(
                scanner,
                userProfileRepo,
                foodItemRepo,
                calculator,
                fileStorageService
        );

        Menu profileMenu = new Menu("profiles", scanner);
        profileMenu.add(returnCommand);
        profileMenu.add(exitCommand);
        profileMenu.add(new Help("Меню для роботи з профілями. 'create', 'select', 'list', 'return'."));
        profileMenu.add(createProfileCmd);
        profileMenu.add(selectProfileCmd);

        Menu mainMenu = new Menu("main", scanner);
        mainMenu.add(exitCommand);
        mainMenu.add(new Help("Головне меню. 'profiles' - робота з профілями, 'exit' - вихід."));
        mainMenu.add(profileMenu);

        displayWelcomeMessage();
        mainMenu.execute();
        displayGoodbyeMessage();
    }

    public void displayWelcomeMessage() {
        System.out.println("=====================================================");
        System.out.println("Вітаємо у вашому персональному помічнику з харчування!");
        System.out.println("=====================================================");
    }

    public void displayGoodbyeMessage() {
        System.out.println("\nДо зустрічі!");
    }

    public void closeScanner() {
        if (this.scanner != null) {
            this.scanner.close();
        }
    }
}