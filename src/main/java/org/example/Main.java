package org.example;

import com.example.nutritionplan.model.SeedData;
import com.example.nutritionplan.model.service.NutritionCalculator;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;

public class Main {
    public static void main(String[] args) {
        try {
            PrintStream out = new PrintStream(System.out, true, "UTF-8");

            out.println("Hello! It is a new project!!!");

            ObjectMapper mapper = new ObjectMapper();
            try (InputStream inputStream = Main.class.getResourceAsStream("/seed_data.json")) {
                if (inputStream == null) {
                    throw new IOException("Cannot find resource file 'seed_data.json'");
                }
                SeedData data = mapper.readValue(inputStream, SeedData.class);

                out.println("Кількість користувачів: " + data.getUsers().size());
                out.println("Перший продукт: " + data.getFood_items().get(0).getName());

                NutritionCalculator calculator = new NutritionCalculator();
                double calories = calculator.calculateDailyCalorieNeeds(data.getUser_profiles().get(0));
                out.println("Рекомендована добова норма калорій для " + data.getUser_profiles().get(0).getFirst_name() + ": " + Math.round(calories) + " ккал");

            } catch (IOException e) {
                System.err.println("Помилка під час читання файлу seed_data.json");
                e.printStackTrace();
            }

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }
}