package com.example.sweater.view.analytics;

/**
 * Представление данных для об аналитике по сообщениям для шаблонизатора
 */
public class MessageAnalyticsView {

    private final String filename;

    private final int downloads;

    /**
     * Конструктор
     *
     * @param filename название файла
     * @param downloads количество загрузок
     */
    public MessageAnalyticsView(final String filename, final Integer downloads) {
        this.filename = filename;
        this.downloads = downloads;
    }

    /**
     * Геттер названия файла
     *
     * @return название файла
     */
    public String getFilename() {
        return filename;
    }

    /**
     * Геттер количества загрузок
     *
     * @return количество загрузок
     */
    public int getDownloads() {
        return downloads;
    }
}
