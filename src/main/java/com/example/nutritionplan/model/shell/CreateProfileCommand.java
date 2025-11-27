package com.example.nutritionplan.model.shell;

import com.example.nutritionplan.model.UserProfile;
import com.example.nutritionplan.model.UserProfileBuilder;
import com.example.nutritionplan.model.service.FileStorageService; // <-- Новий імпорт

import java.util.List;
import java.util.Scanner;

public class CreateProfileCommand implements Command {
    private final Scanner scanner;
    private final List<UserProfile> userProfiles;
    private final FileStorageService storage; // <-- Нове поле для сервісу збереження

    // Конструктор тепер приймає 3 аргументи
    public CreateProfileCommand(Scanner scanner, List<UserProfile> userProfiles, FileStorageService storage) {
        this.scanner = scanner;
        this.userProfiles = userProfiles;
        this.storage = storage;
    }

    @Override
    public Result execute() {
        System.out.println("\n--- Створення нового профілю ---");
        try {
            System.out.print("Ваше ім'я: ");
            String firstName = scanner.nextLine();
            System.out.print("Дата народження (РРРР-ММ-ДД): ");
            String dob = scanner.nextLine();
            System.out.print("Ваша вага в кг: ");
            double weight = Double.parseDouble(scanner.nextLine());
            System.out.print("Ваш зріст в см: ");
            double height = Double.parseDouble(scanner.nextLine());
            System.out.print("Стать (male/female): ");
            String gender = scanner.nextLine();
            System.out.print("Рівень активності (sedentary, light, moderate, active): ");
            String activity = scanner.nextLine();
            System.out.print("Ваша ціль (lose_weight, maintain, gain_muscle): ");
            String goal = scanner.nextLine();

            int newId = userProfiles.stream().mapToInt(UserProfile::getUser_id).max().orElse(0) + 1;

            UserProfile newUser = UserProfileBuilder.create()
                    .userId(newId)
                    .firstName(firstName)
                    .dateOfBirth(dob)
                    .weight(weight)
                    .height(height)
                    .gender(gender)
                    .activityLevel(activity)
                    .goal(goal)
                    .build();

            userProfiles.add(newUser);

            // ---> ВАЖЛИВА ЗМІНА: ЗБЕРІГАЄМО ОДРАЗУ ПІСЛЯ ДОДАВАННЯ <---
            storage.saveUserProfiles(userProfiles);

            System.out.println("\n[Успіх]: Профіль для " + firstName + " створено та збережено!");

        } catch (NumberFormatException e) {
            System.out.println("[Помилка]: Вага та зріст повинні бути числами.");
        } catch (Exception e) {
            System.out.println("[Помилка] під час створення профілю: " + e.getMessage());
        }

        return Result.CONTINUE;
    }

    @Override
    public String name() {
        return "create";
    }
}