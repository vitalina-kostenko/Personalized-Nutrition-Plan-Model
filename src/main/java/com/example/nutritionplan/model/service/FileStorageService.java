package com.example.nutritionplan.model.service;

import com.example.nutritionplan.model.FoodItem;
import com.example.nutritionplan.model.user.UserHistory;
import com.example.nutritionplan.model.user.UserProfile;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class FileStorageService {
    private final ObjectMapper mapper;

    private static final String PROFILES_FILE_PATH = "data/profiles.json";
    private static final String FOOD_DATA_FILE_PATH = "/food_data.json";

    private static final String USER_DATA_DIRECTORY = "data/user_history";

    public FileStorageService() {
        this.mapper = new ObjectMapper();
        mapper.enable(SerializationFeature.INDENT_OUTPUT);
        new File("data").mkdirs();
    }

    public List<FoodItem> loadFoodDatabase() {
        try (InputStream inputStream = getClass().getResourceAsStream(FOOD_DATA_FILE_PATH)) {
            if (inputStream == null) {
                System.err.println("Критична помилка: не знайдено базу даних продуктів " + FOOD_DATA_FILE_PATH);
                return new ArrayList<>();
            }
            return mapper.readValue(inputStream, new TypeReference<List<FoodItem>>() {});
        } catch (IOException e) {
            System.err.println("Критична помилка під час читання бази продуктів: " + e.getMessage());
            e.printStackTrace();
            return new ArrayList<>();
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
            File file = new File(PROFILES_FILE_PATH);
            // Переконаємось, що папка існує
            if (file.getParentFile() != null) {
                file.getParentFile().mkdirs();
            }
            mapper.writeValue(file, profiles);
            System.out.println("\n[Системне повідомлення]: Профілі користувачів збережено.");
        } catch (IOException e) {
            System.err.println("\n[Критична помилка]: Не вдалося зберегти профілі. " + e.getMessage());
        }
    }

    public UserHistory loadUserHistory(int userId) {
        File dir = new File(USER_DATA_DIRECTORY);
        if (!dir.exists()) {
            dir.mkdirs();
        }

        String fileName = "user_" + userId + "_history.json";
        File historyFile = new File(dir, fileName);

        if (historyFile.exists() && historyFile.length() > 0) {
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

        File dir = new File(USER_DATA_DIRECTORY);
        if (!dir.exists()) {
            dir.mkdirs();
        }

        String fileName = "user_" + history.getUserProfile().getUser_id() + "_history.json";
        File historyFile = new File(dir, fileName);

        try {
            mapper.writeValue(historyFile, history);
            System.out.println("\n[Системне повідомлення]: Історію користувача збережено у файл: " + historyFile.getPath());
        } catch (IOException e) {
            System.err.println("\n[Критична помилка]: Не вдалося зберегти історію. " + e.getMessage());
        }
    }
}