package com.example.nutritionplan.model.shell;

import com.example.nutritionplan.model.DailyLog;
import com.example.nutritionplan.model.FoodItem;
import com.example.nutritionplan.model.user.UserHistory;
import com.example.nutritionplan.model.service.NutritionCalculator;

import java.io.PrintStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

public class ShowReportCommand implements Command {

    private final UserHistory history;
    private final NutritionCalculator calculator;
    private final List<FoodItem> foodDatabase;
    private final PrintStream out = System.out;
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public ShowReportCommand(UserHistory history, NutritionCalculator calculator, List<FoodItem> foodDatabase) {
        this.history = history;
        this.calculator = calculator;
        this.foodDatabase = foodDatabase;
    }

    @Override
    public String name() {
        return "show-report";
    }

    @Override
    public Result execute() {
        out.println("\n===== ЗВІТ ЗА ОСТАННІ 7 ДНІВ =====");
        double dailyGoal = calculator.calculateDailyCalorieNeeds(history.getUserProfile());

        out.printf("| %-12s | %-12s | %-15s | %-10s |%n", "Дата", "Ціль (ккал)", "Спожито (ккал)", "Різниця");
        out.println("|--------------|--------------|-----------------|------------|");

        for (int i = 0; i < 7; i++) {
            LocalDate date = LocalDate.now().minusDays(i);
            String dateStr = date.format(DATE_FORMATTER);

            Optional<DailyLog> logOpt = findLogByDate(dateStr);
            if (logOpt.isPresent()) {
                DailyLog log = logOpt.get();
                double totalCalories = calculateTotalCaloriesForLog(log);
                double difference = totalCalories - dailyGoal;
                String differenceStr = String.format("%+.0f", difference);

                out.printf("| %-12s | %-12.0f | %-15.0f | %-10s |%n", dateStr, dailyGoal, totalCalories, differenceStr);
            } else {
                out.printf("| %-12s | %-12.0f | %-15s | %-10s |%n", dateStr, dailyGoal, "Немає даних", "N/A");
            }
        }
        out.println("===============================================================");

        return Result.CONTINUE;
    }

    private Optional<DailyLog> findLogByDate(String date) {
        return history.getDailyLogs().stream().filter(log -> date.equals(log.getDate())).findFirst();
    }

    private double calculateTotalCaloriesForLog(DailyLog log) {
        return log.getMealItems().stream().mapToDouble(item ->
                findFoodById(item.getFood_id())
                        .map(food -> (food.getCalories() / 100.0) * item.getQuantity_g())
                        .orElse(0.0)
        ).sum();
    }

    private Optional<FoodItem> findFoodById(int foodId) {
        return foodDatabase.stream().filter(f -> f.getFood_id().equals(foodId)).findFirst();
    }
}