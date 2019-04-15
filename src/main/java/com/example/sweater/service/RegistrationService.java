package com.example.sweater.service;

import com.example.sweater.domain.User;
import com.example.sweater.domain.dto.CaptchaResponseDto;

/**
 * Сервис регистрации пользователей
 */
public interface RegistrationService {

    /**
     * Получить результаты прохождения капчи от google
     *
     * @param clientCaptchaResponse результаты прохождения капчи на клиенте
     * @return результаты прохождения капчи от google
     */
    CaptchaResponseDto getCaptchaResponse(String clientCaptchaResponse);

    /**
     * Добавить пользователя
     *
     * @param user пользователь
     * @return true/false
     */
    boolean addUser(User user);

    /**
     * Активировать пользователя
     *
     * @param code код активации
     * @return true/false
     */
    boolean activateUser(String code);
}
