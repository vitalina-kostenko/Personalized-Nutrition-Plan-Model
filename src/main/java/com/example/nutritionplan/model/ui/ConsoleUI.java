package com.example.nutritionplan.model.ui;

import com.example.nutritionplan.model.*;
import com.example.nutritionplan.model.service.NutritionCalculator;

import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;
import java.util.stream.Collectors;

public class ConsoleUI {
    private final Scanner scanner;
    private final PrintStream out;
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public ConsoleUI() {
        this.scanner = new Scanner(System.in, "UTF-8");
        PrintStream tempOut;
        try {
            tempOut = new PrintStream(System.out, true, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            System.err.println("Помилка налаштування кодування: " + e.getMessage());
            tempOut = System.out;
        }
        this.out = tempOut;
    }

    public void displayWelcomeMessage() {
        out.println("=====================================================");
        out.println("Вітаємо у вашому персональному помічнику з харчування!");
        out.println("=====================================================");
    }

    public int promptForProfileChoice() {
        out.println("\n--- Меню профілю ---");
        out.println("1. Обрати існуючий профіль");
        out.println("2. Створити новий профіль");
        out.print("Ваш вибір: ");
        return getIntInput();
    }

    public int promptForMainMenuChoice() {
        out.println("\n--- ГОЛОВНЕ МЕНЮ ---");
        out.println("1. Записати сьогоднішнє меню");
        out.println("2. Переглянути звіт за 7 днів");
        out.println("0. Вийти");
        out.print("Ваш вибір: ");
        return getIntInput();
    }

    public UserProfile selectExistingProfile(List<UserProfile> userProfiles) {
        if (userProfiles.isEmpty()) {
            out.println("Збережених профілів не знайдено. Будь ласка, створіть новий.");
            return null;
        }
        out.println("\n--- Оберіть профіль ---");
        for (int i = 0; i < userProfiles.size(); i++) {
            out.println((i + 1) + ". " + userProfiles.get(i).getFirst_name());
        }
        out.print("Ваш вибір: ");
        int profileChoice = getIntInput();

        if (profileChoice > 0 && profileChoice <= userProfiles.size()) {
            UserProfile selectedUser = userProfiles.get(profileChoice - 1);
            out.println("\nЗ поверненням, " + selectedUser.getFirst_name() + "!");
            return selectedUser;
        } else {
            out.println("Невірний номер. Спробуйте ще раз.");
            return null;
        }
    }

    public UserProfile promptForNewUserProfile(int newId) {
        out.println("\n--- РЕЄСТРАЦІЯ НОВОГО КОРИСТУВАЧА ---");
        out.print("Ваше ім'я: ");
        String firstName = scanner.nextLine();
        out.print("Дата народження (РРРР-ММ-ДД): ");
        String dob = scanner.nextLine();
        out.print("Ваша вага в кг (наприклад, 61.5): ");
        double weight = getDoubleInput();
        out.print("Ваш зріст в см (наприклад, 170): ");
        double height = getDoubleInput();
        out.print("Стать (male/female): ");
        String gender = scanner.nextLine();
        out.print("Ваш рівень активності (sedentary, light, moderate, active): ");
        String activity = scanner.nextLine();
        out.print("Ваша ціль (lose_weight, maintain, gain_muscle): ");
        String goal = scanner.nextLine();
        out.println("\nДякуємо за реєстрацію, " + firstName + "!");
        return new UserProfile(newId, firstName, dob, gender, height, weight, activity, goal);
    }

    public void runDailySession(DailyLog dailyLog, UserProfile user, NutritionCalculator calculator, List<FoodItem> foodDatabase) {
        double dailyGoal = calculator.calculateDailyCalorieNeeds(user);
        out.println("\nЗаписуємо дані за " + dailyLog.getDate());
        out.println("Ваша добова норма: " + Math.round(dailyGoal) + " ккал.");

        while (true) {
            printMealMenu();
            int choice = getIntInput();
            String mealType = null;
            switch (choice) {
                case 1:
                    mealType = "breakfast";
                    break;
                case 2:
                    mealType = "lunch";
                    break;
                case 3:
                    mealType = "dinner";
                    break;
                case 4:
                    mealType = "snack";
                    break;
                case 0:
                    break;
                default:
                    out.println("Невірний вибір.");
                    continue;
            }
            if (mealType == null) break;
            handleAddMeal(dailyLog, user, mealType, foodDatabase);
        }
    }

    public void showSevenDaySummary(UserHistory history, NutritionCalculator calculator, List<FoodItem> foodDatabase) {
        out.println("\n===== ЗВІТ ЗА ОСТАННІ 7 ДНІВ =====");
        double dailyGoal = calculator.calculateDailyCalorieNeeds(history.getUserProfile());

        out.printf("| %-12s | %-12s | %-15s | %-10s |%n", "Дата", "Ціль (ккал)", "Спожито (ккал)", "Різниця");
        out.println("|--------------|--------------|-----------------|------------|");

        for (int i = 0; i < 7; i++) {
            LocalDate date = LocalDate.now().minusDays(i);
            String dateStr = date.format(DATE_FORMATTER);

            Optional<DailyLog> logOpt = history.getDailyLogs().stream().filter(log -> dateStr.equals(log.getDate())).findFirst();
            if (logOpt.isPresent()) {
                DailyLog log = logOpt.get();
                double totalCalories = calculateTotalCaloriesForLog(log, foodDatabase);
                double difference = totalCalories - dailyGoal;
                String differenceStr = String.format("%+.0f", difference);
                out.printf("| %-12s | %-12.0f | %-15.0f | %-10s |%n", dateStr, dailyGoal, totalCalories, differenceStr);
            } else {
                out.printf("| %-12s | %-12.0f | %-15s | %-10s |%n", dateStr, dailyGoal, "Немає даних", "N/A");
            }
        }
        out.println("===============================================================");
    }

    public void showTodaysDetailedLog(DailyLog dailyLog, List<FoodItem> foodDatabase) {
        if (dailyLog.getMealItems().isEmpty()) {
            out.println("Ви ще нічого не додали за сьогодні.");
            return;
        }

        out.println("\n--- Детальний звіт за " + dailyLog.getDate() + " ---");
        out.printf("| %-35s | %-10s | %-10s |%n", "Продукт", "Вага (г)", "Калорії");
        out.println("|-------------------------------------|------------|------------|");

        for (MealEntry mealEntry : dailyLog.getMealEntries()) {
            out.printf("| %-59s |%n", ">> " + mealEntry.getMeal_type().toUpperCase());
            dailyLog.getMealItems().stream()
                    .filter(item -> item.getMeal_id().equals(mealEntry.getMeal_id()))
                    .forEach(item -> {
                        findFoodById(foodDatabase, item.getFood_id()).ifPresent(food -> {
                            double caloriesInPortion = (food.getCalories() / 100.0) * item.getQuantity_g();
                            out.printf("| %-35s | %-10.1f | %-10.0f |%n", food.getName(), item.getQuantity_g(), caloriesInPortion);
                        });
                    });
        }

        double totalCalories = calculateTotalCaloriesForLog(dailyLog, foodDatabase);
        out.println("|-------------------------------------|------------|------------|");
        out.printf("| %-48s | %-10.0f |%n", "ВСЬОГО ЗА ДЕНЬ:", totalCalories);
        out.println("---------------------------------------------------------------");
    }

    private void handleAddMeal(DailyLog dailyLog, UserProfile user, String mealType, List<FoodItem> foodDatabase) {
        out.println("\n--- Додавання продуктів для: " + mealType.toUpperCase() + " ---");
        List<FoodItem> recommendedFoods = foodDatabase.stream()
                .filter(food -> food.getCategories() != null && food.getCategories().contains(mealType))
                .collect(Collectors.toList());

        if (recommendedFoods.isEmpty()) {
            out.println("На жаль, рекомендованих продуктів для цього прийому їжі не знайдено.");
            return;
        }

        int newMealId = getNewMealId(dailyLog);
        MealEntry newMealEntry = new MealEntry(newMealId, user.getUser_id(), mealType, java.time.ZonedDateTime.now().toString());
        dailyLog.getMealEntries().add(newMealEntry);

        while (true) {
            out.println("\nОберіть продукт зі списку (введіть номер):");
            for (int i = 0; i < recommendedFoods.size(); i++) {
                out.println((i + 1) + ". " + recommendedFoods.get(i).getName());
            }
            out.println("0. Завершити додавання до цього прийому їжі");
            out.print("Ваш вибір: ");

            int choice = getIntInput();
            if (choice == 0) break;

            if (choice > 0 && choice <= recommendedFoods.size()) {
                FoodItem chosenFood = recommendedFoods.get(choice - 1);
                out.print("Введіть вагу для '" + chosenFood.getName() + "' (грами): ");
                double weight = getDoubleInput();
                MealItem newMealItem = new MealItem(getNewMealItemId(dailyLog), newMealId, chosenFood.getFood_id(), weight);
                dailyLog.getMealItems().add(newMealItem);
                out.println("-> Додано.");
            } else {
                out.println("Невірний номер.");
            }
        }
    }

    public void displayGoodbyeMessage() {
        out.println("До зустрічі!");
    }

    public void displayInvalidChoice() {
        out.println("Невірний вибір.");
    }

    private void printMealMenu() {
        out.println("\n--- Меню прийомів їжі ---");
        out.println("1. Додати сніданок");
        out.println("2. Додати обід");
        out.println("3. Додати вечерю");
        out.println("4. Додати перекус");
        out.println("0. Завершити додавання до цього дня");
        out.print("Ваш вибір: ");
    }

    private int getNewMealId(DailyLog log) {
        return log.getMealEntries().stream().mapToInt(MealEntry::getMeal_id).max().orElse(0) + 1;
    }

    private int getNewMealItemId(DailyLog log) {
        return log.getMealItems().stream().mapToInt(MealItem::getMeal_item_id).max().orElse(0) + 1;
    }

    private Optional<FoodItem> findFoodById(List<FoodItem> foodDatabase, int foodId) {
        return foodDatabase.stream()
                .filter(food -> food.getFood_id() == foodId)
                .findFirst();
    }

    private double calculateTotalCaloriesForLog(DailyLog log, List<FoodItem> foodDatabase) {
        return log.getMealItems().stream().mapToDouble(item -> {
            Optional<FoodItem> foodOpt = findFoodById(foodDatabase, item.getFood_id());
            if (foodOpt.isPresent()) {
                FoodItem food = foodOpt.get();
                return (food.getCalories() / 100.0) * item.getQuantity_g();
            }
            return 0;
        }).sum();
    }

    private int getIntInput() {
        try {
            int choice = scanner.nextInt();
            scanner.nextLine();
            return choice;
        } catch (Exception e) {
            out.println("Невірний ввід. Будь ласка, введіть число.");
            scanner.nextLine();
            return -1;
        }
    }

    private double getDoubleInput() {
        try {
            double value = scanner.nextDouble();
            scanner.nextLine();
            return value;
        } catch (Exception e) {
            out.println("Невірний ввід. Будь ласка, введіть число (наприклад, 65.5).");
            scanner.nextLine();
            return -1.0;
        }
    }

    public void closeScanner() {
        scanner.close();
    }
}