package com.example.sweater;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Главный файл приложения
 */
@SpringBootApplication
public class Application {
    /**
     * Точка входа в приложение
     *
     * @param args аргументы
     */
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}