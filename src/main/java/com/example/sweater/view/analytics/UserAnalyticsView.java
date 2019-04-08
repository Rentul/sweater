package com.example.sweater.view.analytics;

public class UserAnalyticsView {

    private int id;

    private final String username;

    private final int amountOfFiles;

    private final int numberOfDownloads;

    private final int numberOfMessages;

    public UserAnalyticsView(int id, String username, int amountOfFiles, int numberOfDownloads, int numberOfMessages) {
        this.id = id;
        this.username = username;
        this.amountOfFiles = amountOfFiles;
        this.numberOfDownloads = numberOfDownloads;
        this.numberOfMessages = numberOfMessages;
    }

    public int getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public int getAmountOfFiles() {
        return amountOfFiles;
    }

    public int getNumberOfDownloads() {
        return numberOfDownloads;
    }

    public int getNumberOfMessages() {
        return numberOfMessages;
    }
}
