package com.example.sweater.service;

import com.example.sweater.domain.User;

import java.util.List;
import java.util.Map;

/**
 * Сервис пользователей
 */
public interface UserService {

    /**
     * Получить список всех пользователей
     *
     * @return список всех пользователей
     */
    List<User> findAll();

    /**
     * Сохранить пользоватея после редактирования его роли
     *
     * @param user пользователь
     * @param username имя пользователя
     * @param form форма с данными пользователя
     * @return true/false
     */
    boolean saveUser(User user, String username, Map<String, String> form);

    /**
     * Обновить данные пользователя
     *
     * @param user пользователь
     * @param password пароль
     * @param email электронная почта
     */
    void updateProfile(User user, String password, String email);

    /**
     * Подписаться на пользователя
     *
     * @param currentUser текущий пользователя
     * @param user пользователя, на которого оформляется подписка
     */
    void subscribe(User currentUser, User user);

    /**
     * Отписаться от пользователя
     *
     * @param currentUser текущий пользователь
     * @param user пользователь, от которого отписываются
     */
    void unsubscribe(User currentUser, User user);
}
