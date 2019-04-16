package com.example.sweater.view.analytics;

/**
 * Представление данных об аналитике пользователей для шаблонизатора
 */
public class UserAnalyticsView {

    private final int id;

    private final String username;

    private final int amountOfFiles;

    private final int numberOfDownloads;

    private final int numberOfMessages;

    /**
     * Конструктор
     *
     * @param id идентификатор пользователя
     * @param username имя пользователя
     * @param amountOfFiles количество файлов пользователя
     * @param numberOfDownloads количество загрузок файлов этого пользователя
     * @param numberOfMessages количество сообщений
     */
    public UserAnalyticsView(int id, String username, int amountOfFiles, int numberOfDownloads, int numberOfMessages) {
        this.id = id;
        this.username = username;
        this.amountOfFiles = amountOfFiles;
        this.numberOfDownloads = numberOfDownloads;
        this.numberOfMessages = numberOfMessages;
    }

    /**
     * Геттер идентификатора пользователя
     *
     * @return идентификатор пользователя
     */
    public int getId() {
        return id;
    }

    /**
     * Геттер имени пользователя
     *
     * @return имя пользователя
     */
    public String getUsername() {
        return username;
    }

    /**
     * Геттер количества файлов пользователя
     *
     * @return количество файлов пользователя
     */
    public int getAmountOfFiles() {
        return amountOfFiles;
    }

    /**
     * Геттер количества загрузок файлов пользователя
     *
     * @return количество загрузок файлов пользователя
     */
    public int getNumberOfDownloads() {
        return numberOfDownloads;
    }

    /**
     * Геттер количества сообщений пользователя
     *
     * @return количество сообщений пользователя
     */
    public int getNumberOfMessages() {
        return numberOfMessages;
    }
}
