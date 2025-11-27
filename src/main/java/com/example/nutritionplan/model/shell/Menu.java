package com.example.nutritionplan.model.shell;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
public class Menu implements Command {
    private final String name;
    private final Scanner scanner;
    private final Map<String, Command> commands = new HashMap<>();

    public Menu(String name, Scanner scanner) {
        this.name = name;
        this.scanner = scanner;
    }

    @Override
    public Result execute() {
        if (commands.isEmpty()) {
            System.out.println("Menu is empty. Returning");
            return Result.CONTINUE;
        }
        Result result;
        do {
            prompt();
            String commandName = scanner.nextLine();
            Command command = commands.get(commandName);
            if (command != null) {
                result = command.execute();
            } else {
                System.out.println("Command not found. Try again");
                result = Result.CONTINUE;
            }
        } while (result == Result.CONTINUE);
        return result == Result.EXIT ? Result.EXIT : Result.CONTINUE;
    }

    @Override public String name() { return name; }
    public void add(Command command) { commands.put(command.name(), command); }
    private void prompt() {
        String commandNames = String.join(", ", commands.keySet());
        System.out.println("\nEnter one of the commands: " + commandNames);
        System.out.print(name() + "> ");
    }
}