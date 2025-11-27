package com.example.nutritionplan.model.shell;

import com.example.nutritionplan.model.*;
import com.example.nutritionplan.model.service.NutritionCalculator;

import java.io.PrintStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;
import java.util.stream.Collectors;

public class LogFoodCommand implements Command {

    private final Scanner scanner;
    private final UserHistory history;
    private final NutritionCalculator calculator;
    private final List<FoodItem> foodDatabase;
    private final PrintStream out = System.out;
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public LogFoodCommand(Scanner scanner, UserHistory history, NutritionCalculator calculator, List<FoodItem> foodDatabase) {
        this.scanner = scanner;
        this.history = history;
        this.calculator = calculator;
        this.foodDatabase = foodDatabase;
    }

    @Override
    public String name() {
        return "log-food";
    }

    @Override
    public Result execute() {
        LocalDate today = LocalDate.now();
        String todayStr = today.format(DATE_FORMATTER);

        DailyLog todayLog = findLogByDate(todayStr).orElseGet(() -> {
            DailyLog newLog = new DailyLog(todayStr);
            history.getDailyLogs().add(newLog);
            return newLog;
        });

        runDailySession(todayLog);
        showTodaysDetailedLog(todayLog);

        return Result.CONTINUE;
    }

    private void runDailySession(DailyLog dailyLog) {
        UserProfile user = history.getUserProfile();
        double dailyGoal = calculator.calculateDailyCalorieNeeds(user);
        out.println("\nЗаписуємо дані за " + dailyLog.getDate());
        out.println("Ваша добова норма: " + Math.round(dailyGoal) + " ккал.");

        while (true) {
            printMealMenu();
            String input = scanner.nextLine();
            int choice;
            try {
                choice = Integer.parseInt(input);
            } catch (NumberFormatException e) {
                out.println("Невірний ввід. Будь ласка, введіть число.");
                continue;
            }

            String mealType = null;
            switch (choice) {
                case 1: mealType = "breakfast"; break;
                case 2: mealType = "lunch"; break;
                case 3: mealType = "dinner"; break;
                case 4: mealType = "snack"; break;
                case 0: break;
                default:
                    out.println("Невірний вибір.");
                    continue;
            }
            if (mealType == null) break;
            handleAddMeal(dailyLog, user, mealType);
        }
    }

    private void handleAddMeal(DailyLog dailyLog, UserProfile user, String mealType) {
        out.println("\n--- Додавання продуктів для: " + mealType.toUpperCase() + " ---");
        List<FoodItem> recommendedFoods = foodDatabase.stream()
                .filter(food -> food.getCategories() != null && food.getCategories().contains(mealType))
                .collect(Collectors.toList());

        if (recommendedFoods.isEmpty()) {
            out.println("Рекомендованих продуктів для цього прийому їжі не знайдено.");
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

            String input = scanner.nextLine();
            int choice;
            try {
                choice = Integer.parseInt(input);
            } catch (NumberFormatException e) {
                out.println("Невірний ввід. Будь ласка, введіть число.");
                continue;
            }

            if (choice == 0) break;

            if (choice > 0 && choice <= recommendedFoods.size()) {
                FoodItem chosenFood = recommendedFoods.get(choice - 1);
                out.print("Введіть вагу для '" + chosenFood.getName() + "' (грами): ");

                double weight;
                try {
                    weight = Double.parseDouble(scanner.nextLine());
                } catch (NumberFormatException e) {
                    out.println("Невірний ввід ваги. Спробуйте ще раз.");
                    continue;
                }

                MealItem newMealItem = new MealItem(getNewMealItemId(dailyLog), newMealId, chosenFood.getFood_id(), weight);
                dailyLog.getMealItems().add(newMealItem);
                out.println("-> Додано.");
            } else {
                out.println("Невірний номер.");
            }
        }
    }

    private void showTodaysDetailedLog(DailyLog dailyLog) {
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
                        findFoodById(item.getFood_id()).ifPresent(food -> {
                            double caloriesInPortion = (food.getCalories() / 100.0) * item.getQuantity_g();
                            out.printf("| %-35s | %-10.1f | %-10.0f |%n", food.getName(), item.getQuantity_g(), caloriesInPortion);
                        });
                    });
        }

        double totalCalories = calculateTotalCaloriesForLog(dailyLog);
        out.println("|-------------------------------------|------------|------------|");
        out.printf("| %-48s | %-10.0f |%n", "ВСЬОГО ЗА ДЕНЬ:", totalCalories);
        out.println("---------------------------------------------------------------");
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

    private double calculateTotalCaloriesForLog(DailyLog log) {
        return log.getMealItems().stream().mapToDouble(item -> {
            return findFoodById(item.getFood_id())
                    .map(food -> (food.getCalories() / 100.0) * item.getQuantity_g())
                    .orElse(0.0);
        }).sum();
    }

    private Optional<FoodItem> findFoodById(int foodId) {
        return foodDatabase.stream().filter(f -> f.getFood_id().equals(foodId)).findFirst();
    }

    private Optional<DailyLog> findLogByDate(String date) {
        return history.getDailyLogs().stream().filter(log -> date.equals(log.getDate())).findFirst();
    }

    private int getNewMealId(DailyLog log) {
        return log.getMealEntries().stream().mapToInt(MealEntry::getMeal_id).max().orElse(0) + 1;
    }

    private int getNewMealItemId(DailyLog log) {
        return log.getMealItems().stream().mapToInt(MealItem::getMeal_item_id).max().orElse(0) + 1;
    }
}