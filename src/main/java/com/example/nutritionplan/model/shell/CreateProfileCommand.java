package com.example.nutritionplan.model.shell;

import com.example.nutritionplan.model.user.UserProfile;
import com.example.nutritionplan.model.repository.UserProfileRepository;

import java.util.Scanner;

public class CreateProfileCommand implements Command {

    private final Scanner scanner;
    private final UserProfileRepository userProfileRepository;

    public CreateProfileCommand(Scanner scanner, UserProfileRepository userProfileRepository) {
        this.scanner = scanner;
        this.userProfileRepository = userProfileRepository;
    }

    @Override
    public Result execute() {
        System.out.println("\n--- Створення нового профілю ---");

        try {
            System.out.print("Ваше ім'я: ");
            String firstName = scanner.nextLine().trim();
            if (firstName.isEmpty()) {
                System.out.println("Ім'я не може бути пустим.");
                return Result.CONTINUE;
            }

            System.out.print("Дата народження (РРРР-ММ-ДД): ");
            String dob = scanner.nextLine().trim();

            System.out.print("Ваша вага в кг: ");
            double weight = Double.parseDouble(scanner.nextLine().trim().replace(",", "."));

            System.out.print("Ваш зріст в см: ");
            double height = Double.parseDouble(scanner.nextLine().trim().replace(",", "."));

            System.out.print("Стать (male/female): ");
            String genderInput = scanner.nextLine().trim().toUpperCase();

            String gender = genderInput.startsWith("M") ? "MALE" : "FEMALE";

            System.out.print("Рівень активності (sedentary, light, moderate, active): ");
            String activityInput = scanner.nextLine().trim().toUpperCase().replace(" ", "_");

            String activityLevel = validateActivity(activityInput);

            System.out.print("Ваша ціль (lose_weight, maintain, gain_muscle): ");
            String goalInput = scanner.nextLine().trim().toUpperCase().replace(" ", "_");
            String goal = validateGoal(goalInput);

            UserProfile newUser = new UserProfile();
            newUser.setFirst_name(firstName);
            newUser.setDate_of_birth(dob);
            newUser.setWeight_kg(weight);
            newUser.setHeight_cm(height);
            newUser.setGender(gender);
            newUser.setActivity_level(activityLevel);
            newUser.setGoal(goal);

            UserProfile savedUser = userProfileRepository.save(newUser);

            System.out.println("\n[Успіх] Профіль створено! Ваш ID: " + savedUser.getUser_id());
            System.out.println("Тепер ви можете вибрати цей профіль командою 'select'.");

        } catch (NumberFormatException e) {
            System.out.println("[Помилка] Введено некоректне число. Використовуйте крапку для дробових чисел.");
        } catch (Exception e) {
            System.out.println("[Критична помилка] Не вдалося створити профіль: " + e.getMessage());
            e.printStackTrace();
        }

        return Result.CONTINUE;
    }

    private String validateActivity(String input) {
        if (input.contains("SEDENTARY")) return "SEDENTARY";
        if (input.contains("LIGHT")) return "LIGHT_ACTIVE";
        if (input.contains("MODERATE")) return "MODERATE_ACTIVE";
        if (input.contains("ACTIVE")) return "VERY_ACTIVE";
        return input;
    }

    private String validateGoal(String input) {
        if (input.contains("LOSE")) return "LOSE_WEIGHT";
        if (input.contains("MAINTAIN")) return "MAINTAIN_WEIGHT";
        if (input.contains("GAIN")) return "GAIN_MUSCLE";
        return input;
    }

    @Override
    public String name() {
        return "create";
    }
}