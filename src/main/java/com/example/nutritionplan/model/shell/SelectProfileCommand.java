package com.example.nutritionplan.model.shell;

import com.example.nutritionplan.model.FoodItem;
import com.example.nutritionplan.model.repository.FoodItemRepository;
import com.example.nutritionplan.model.repository.UserProfileRepository;
import com.example.nutritionplan.model.user.UserHistory;
import com.example.nutritionplan.model.user.UserProfile;
import com.example.nutritionplan.model.service.FileStorageService;
import com.example.nutritionplan.model.service.NutritionCalculator;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Scanner;

@Component
public class SelectProfileCommand implements Command {
    private final Scanner scanner;
    private final UserProfileRepository userProfileRepo;
    private final FoodItemRepository foodItemRepo;
    private final NutritionCalculator calculator;
    private final FileStorageService storage;

    public SelectProfileCommand(Scanner scanner, UserProfileRepository userProfileRepo, FoodItemRepository foodItemRepo, NutritionCalculator calculator, FileStorageService storage) {
        this.scanner = scanner;
        this.userProfileRepo = userProfileRepo;
        this.foodItemRepo = foodItemRepo;
        this.calculator = calculator;
        this.storage = storage;
    }

    @Override
    public Result execute() {
        List<UserProfile> userProfiles = userProfileRepo.findAll();
        List<FoodItem> foodDatabase = foodItemRepo.findAll();

        if (userProfiles.isEmpty()) {
            System.out.println("Збережених профілів не знайдено. Будь ласка, створіть новий за допомогою команди 'create'.");
            return Result.CONTINUE;
        }

        System.out.println("\n--- Оберіть профіль ---");
        for (int i = 0; i < userProfiles.size(); i++) {
            System.out.println((i + 1) + ". " + userProfiles.get(i).getFirst_name());
        }
        System.out.print("Ваш вибір: ");

        try {
            int choice = Integer.parseInt(scanner.nextLine());
            if (choice > 0 && choice <= userProfiles.size()) {
                UserProfile selectedUser = userProfiles.get(choice - 1);

                UserHistory history = storage.loadUserHistory(selectedUser.getUser_id());
                if (history.getUserProfile() == null) {
                    history.setUserProfile(selectedUser);
                }

                System.out.println("\nОбрано профіль: " + selectedUser.getFirst_name());

                Menu userSessionMenu = new Menu(selectedUser.getFirst_name().toLowerCase(), scanner);
                userSessionMenu.add(new Return());
                userSessionMenu.add(new Help("Меню сесії. 'log-food' - записати їжу, 'show-report' - звіт."));

                userSessionMenu.add(new LogFoodCommand(scanner, history, calculator, foodDatabase));
                userSessionMenu.add(new ShowReportCommand(history, calculator, foodDatabase));
                userSessionMenu.execute();

                storage.saveUserHistory(history);
                System.out.println("\nПовернення до меню профілів...");
                return Result.CONTINUE;

            } else {
                System.out.println("Невірний номер. Спробуйте ще раз.");
            }
        } catch (NumberFormatException e) {
            System.out.println("Будь ласка, введіть число.");
        }
        return Result.CONTINUE;
    }

    @Override
    public String name() {
        return "select";
    }
}