package org.example;

import com.example.nutritionplan.model.DailyLog;
import com.example.nutritionplan.model.FoodItem;
import com.example.nutritionplan.model.UserHistory;
import com.example.nutritionplan.model.UserProfile;
import com.example.nutritionplan.model.service.NutritionCalculator;
import com.example.nutritionplan.model.service.FileStorageService;
import com.example.nutritionplan.model.ui.ConsoleUI;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

public class Application {
    private final ConsoleUI ui;
    private final FileStorageService storage;
    private final NutritionCalculator calculator;

    private List<FoodItem> foodDatabase;
    private List<UserProfile> userProfiles;
    private UserProfile currentUser;
    private UserHistory currentUserHistory;

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public Application(ConsoleUI ui, FileStorageService storage, NutritionCalculator calculator) {
        this.ui = ui;
        this.storage = storage;
        this.calculator = calculator;
    }


    public void run() {
        if (!loadInitialData()) {
            return;
        }

        ui.displayWelcomeMessage();

        this.currentUser = selectOrCreateUserProfile();
        this.currentUserHistory = storage.loadUserHistory(currentUser.getUser_id());
        if (currentUserHistory.getUserProfile() == null) {
            currentUserHistory.setUserProfile(currentUser);
        }

        runMainSession();

        storage.saveUserHistory(currentUserHistory);
        storage.saveUserProfiles(userProfiles);
        ui.closeScanner();
    }


    private boolean loadInitialData() {
        this.foodDatabase = storage.loadFoodDatabase();
        this.userProfiles = storage.loadUserProfiles();
        return foodDatabase != null;
    }


    private UserProfile selectOrCreateUserProfile() {
        while (true) {
            int choice = ui.promptForProfileChoice();
            if (choice == 1) {
                UserProfile selected = ui.selectExistingProfile(userProfiles);
                if (selected != null) return selected;
            } else if (choice == 2) {
                int newId = userProfiles.stream().mapToInt(UserProfile::getUser_id).max().orElse(0) + 1;
                UserProfile newUser = ui.promptForNewUserProfile(newId);
                userProfiles.add(newUser);
                return newUser;
            } else {
                ui.displayInvalidChoice();
            }
        }
    }


    private void runMainSession() {
        while (true) {
            int choice = ui.promptForMainMenuChoice();

            if (choice == 1) {
                logTodaysFood();
            } else if (choice == 2) {
                ui.showSevenDaySummary(currentUserHistory, calculator, foodDatabase);
            } else if (choice == 0) {
                ui.displayGoodbyeMessage();
                break; // Виходимо з циклу і завершуємо програму
            } else {
                ui.displayInvalidChoice();
            }
        }
    }


    private void logTodaysFood() {
        LocalDate today = LocalDate.now();
        String todayStr = today.format(DATE_FORMATTER);

        DailyLog todayLog = findLogByDate(currentUserHistory, todayStr).orElseGet(() -> {
            DailyLog newLog = new DailyLog(todayStr);
            currentUserHistory.getDailyLogs().add(newLog);
            return newLog;
        });

        ui.runDailySession(todayLog, currentUser, calculator, foodDatabase);
        ui.showTodaysDetailedLog(todayLog, foodDatabase);
    }


    private Optional<DailyLog> findLogByDate(UserHistory history, String date) {
        return history.getDailyLogs().stream().filter(log -> date.equals(log.getDate())).findFirst();
    }
}