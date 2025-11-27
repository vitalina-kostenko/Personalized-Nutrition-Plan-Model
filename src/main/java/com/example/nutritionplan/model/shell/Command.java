package com.example.nutritionplan.model.shell;

public interface Command {
    Result execute();
    String name();
}