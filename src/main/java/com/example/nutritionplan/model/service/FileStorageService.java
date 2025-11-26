package com.example.nutritionplan.model.service;

import com.example.nutritionplan.model.FoodItem;
import com.example.nutritionplan.model.UserHistory;
import com.example.nutritionplan.model.UserProfile;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.example.Main;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class FileStorageService {
    private final ObjectMapper mapper;
    private static final String PROFILES_FILE_PATH = "data/profiles.json";
    private static final String FOOD_DATA_FILE_PATH = "/food_data.json";
    private static final String USER_DATA_DIRECTORY = "data/user_session.json";

    public FileStorageService() {
        this.mapper = new ObjectMapper();
        mapper.enable(SerializationFeature.INDENT_OUTPUT);
    }

    public List<FoodItem> loadFoodDatabase() {
        try (InputStream inputStream = Main.class.getResourceAsStream(FOOD_DATA_FILE_PATH)) {
            if (inputStream == null) {
                System.err.println("Критична помилка: не знайдено базу даних продуктів " + FOOD_DATA_FILE_PATH);
                return null;
            }
            return mapper.readValue(inputStream, new TypeReference<List<FoodItem>>() {});
        } catch (IOException e) {
            System.err.println("Критична помилка під час читання бази продуктів: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    public List<UserProfile> loadUserProfiles() {
        File profilesFile = new File(PROFILES_FILE_PATH);
        if (profilesFile.exists() && profilesFile.length() > 0) {
            try {
                return mapper.readValue(profilesFile, new TypeReference<List<UserProfile>>() {});
            } catch (IOException e) {
                System.err.println("Помилка читання файлу профілів: " + e.getMessage());
            }
        }
        return new ArrayList<>();
    }

    public void saveUserProfiles(List<UserProfile> profiles) {
        try {
            mapper.writeValue(new File(PROFILES_FILE_PATH), profiles);
            System.out.println("\n[Системне повідомлення]: Профілі користувачів збережено.");
        } catch (IOException e) {
            System.err.println("\n[Критична помилка]: Не вдалося зберегти профілі. " + e.getMessage());
        }
    }


    //не зчитує з файлу і не створює його
    public UserHistory loadUserHistory(int userId) {
        String fileName = "user_" + userId + "_history.json";
        File historyFile = new File(USER_DATA_DIRECTORY, fileName);
        new File(USER_DATA_DIRECTORY).mkdirs();
        if (historyFile.exists()) {
            try {
                return mapper.readValue(historyFile, UserHistory.class);
            } catch (IOException e) {
                System.err.println("Помилка читання файлу історії " + historyFile.getPath() + ". Створюємо нову. " + e.getMessage());
            }
        }
        return new UserHistory();
    }

    public void saveUserHistory(UserHistory history) {
        if (history.getUserProfile() == null) return;
        String fileName = "user_" + history.getUserProfile().getUser_id() + "_history.json";
        File historyFile = new File(USER_DATA_DIRECTORY, fileName);
        try {
            mapper.writeValue(historyFile, history);
            System.out.println("\n[Системне повідомлення]: Історію користувача збережено у файл: " + historyFile.getPath());
        } catch (IOException e) {
            System.err.println("\n[Критична помилка]: Не вдалося зберегти історію. " + e.getMessage());
        }
    }
}