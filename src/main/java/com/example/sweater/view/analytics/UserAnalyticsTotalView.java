package com.example.sweater.view.analytics;

/**
 * Представление данных итоговой аналитики по пользователям для шаблонизатора
 */
public class UserAnalyticsTotalView {

    private int userCount;

    private int messageCount;

    private int fileCount;

    private int downloadCount;

    /**
     * Конструктор
     *
     * @param userCount количество пользователей
     * @param messageCount количество собщений
     * @param fileCount количество файлов
     * @param downloadCount количество файлов
     */
    public UserAnalyticsTotalView(int userCount, int messageCount, int fileCount, int downloadCount) {
        this.userCount = userCount;
        this.messageCount = messageCount;
        this.fileCount = fileCount;
        this.downloadCount = downloadCount;
    }

    /**
     * Геттер количества пользователей
     *
     * @return количество пользователей
     */
    public int getUserCount() {
        return userCount;
    }

    /**
     * Геттер количества сообщений
     *
     * @return количество сообщений
     */
    public int getMessageCount() {
        return messageCount;
    }

    /**
     * Геттер количества файлов
     *
     * @return количество файлов
     */
    public int getFileCount() {
        return fileCount;
    }

    /**
     * Геттер количества скачиваний
     *
     * @return количество скачиваний
     */
    public int getDownloadCount() {
        return downloadCount;
    }
}
