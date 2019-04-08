package com.example.sweater.view.analytics;

public class UserAnalyticsTotalView {

    private int userCount;

    private int messageCount;

    private int fileCount;

    private int downloadCount;

    public UserAnalyticsTotalView(int userCount, int messageCount, int fileCount, int downloadCount) {
        this.userCount = userCount;
        this.messageCount = messageCount;
        this.fileCount = fileCount;
        this.downloadCount = downloadCount;
    }

    public int getUserCount() {
        return userCount;
    }

    public int getMessageCount() {
        return messageCount;
    }

    public int getFileCount() {
        return fileCount;
    }

    public int getDownloadCount() {
        return downloadCount;
    }
}
