package com.example.sweater.view.analytics;

public class MessageAnalyticsView {

    private final String filename;

    private final int downloads;

    public MessageAnalyticsView(String filename, int downloads) {
        this.filename = filename;
        this.downloads = downloads;
    }

    public String getFilename() {
        return filename;
    }

    public int getDownloads() {
        return downloads;
    }
}
