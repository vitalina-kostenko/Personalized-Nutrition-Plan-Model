package org.example;

import com.example.nutritionplan.model.FoodItem;
import com.example.nutritionplan.model.service.FileStorageService;
import com.example.nutritionplan.model.service.NutritionCalculator;
import com.example.nutritionplan.model.ui.ConsoleUI;
import com.example.nutritionplan.model.repository.FoodItemRepository;
import com.example.nutritionplan.model.repository.UserProfileRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import java.util.List;

@SpringBootApplication(scanBasePackages = {"com.example.nutritionplan", "org.example"})
@EnableJpaRepositories("com.example.nutritionplan.model.repository")
@EntityScan("com.example.nutritionplan.model")
public class Application implements CommandLineRunner {

    private final ConsoleUI ui;
    private final UserProfileRepository userProfileRepository;
    private final FoodItemRepository foodItemRepository;
    private final NutritionCalculator calculator;
    private final FileStorageService fileStorageService;

    public Application(ConsoleUI ui,
                       UserProfileRepository userProfileRepository,
                       FoodItemRepository foodItemRepository,
                       NutritionCalculator calculator,
                       FileStorageService fileStorageService) {
        this.ui = ui;
        this.userProfileRepository = userProfileRepository;
        this.foodItemRepository = foodItemRepository;
        this.calculator = calculator;
        this.fileStorageService = fileStorageService;
    }

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        // --- ПОЧАТОК НОВОГО КОДУ ---

        // 1. Перевіряємо, чи база пуста
        long count = foodItemRepository.count();

        if (count == 0) {
            System.out.println(">>> База даних пуста. Починаю завантаження продуктів з файлу...");

            // 2. Завантажуємо список з JSON
            List<FoodItem> items = fileStorageService.loadFoodDatabase();

            // 3. Зберігаємо в базу даних
            if (items != null && !items.isEmpty()) {
                foodItemRepository.saveAll(items);
                System.out.println(">>> Успішно додано " + items.size() + " продуктів у базу!");
            } else {
                System.out.println(">>> Помилка: Не вдалося завантажити продукти з файлу.");
            }
        } else {
            System.out.println(">>> Продукти вже є в базі (" + count + " шт).");
        }

        // --- КІНЕЦЬ НОВОГО КОДУ ---

        ui.start(userProfileRepository, foodItemRepository, calculator, fileStorageService);
        ui.closeScanner();
    }
}