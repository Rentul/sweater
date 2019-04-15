package com.example.sweater.service;

import com.example.sweater.domain.Message;
import com.example.sweater.domain.User;
import com.example.sweater.view.analytics.MessageAnalyticsView;
import com.example.sweater.view.analytics.UserAnalyticsTotalView;
import com.example.sweater.view.analytics.UserAnalyticsView;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * Основной сервис
 */
public interface MainService {

    /**
     * Сохранить сообщение
     *
     * @param user пользователь
     * @param message сообщение
     * @param file файл, прикрепленный к сообщению
     * @throws IOException
     */
    void saveMessage(User user, Message message, MultipartFile file) throws IOException;

    /**
     * Удалить сообщение
     *
     * @param message сообщение
     */
    void deleteMessage(Message message);

    /**
     * Редактировать сообщение
     *
     * @param currentUser текущий пользователь
     * @param message сообщение
     * @param text текст сообщения
     * @param tag тег
     * @param file файл, прикрепленный к сообщению
     * @throws IOException
     */
    void updateMessage(User currentUser,
                       Message message,
                       String text,
                       String tag,
                       MultipartFile file) throws IOException;

    /**
     * Скачать файл с сервера
     *
     * @param message сообщение, к которому прикреплен файл
     * @param response ответ сервера, содержащий сообщение
     * @throws Exception
     */
    void downloadFile(Message message, HttpServletResponse response) throws Exception;

    /**
     * Получить отфильтрованные сообщения
     *
     * @param filter фильтр
     * @return отфильтрованные сообщения
     */
    Iterable<Message> getFilteredMessages(String filter);

    /**
     * Получить список всех пользователей
     *
     * @return список всех польщователей
     */
    List<User> findAllUsers();

    /**
     * Получить аналитику по всем пользователям
     *
     * @return аналитика по всем пользователям
     */
    List<UserAnalyticsView> getAnalytics();

    /**
     * Получить общие итоги по аналитике всех пользователей
     *
     * @param analyticsViews аналитика пользователей
     * @return аналитика общих итогов
     */
    UserAnalyticsTotalView getTotalsForAnalytics(List<UserAnalyticsView> analyticsViews);

    /**
     * Получить детальную аналитику для одного пользователя по его файлам
     *
     * @param user пользователь
     * @return аналитика конкретного пользователя и его файлов
     */
    List<MessageAnalyticsView> getUserAnalyticsByFiles(User user);

}
