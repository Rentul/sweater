package com.example.sweater.controller.utils;

import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.util.Map;
import java.util.stream.Collector;
import java.util.stream.Collectors;

/**
 * Утилиты контроллеров
 */
public class ControllerUtils {

    /**
     * Получить ошибки связки
     *
     * @param bindingResult результаты связки
     * @return словарь ошибок
     */
    public static Map<String, String> getErrors(final BindingResult bindingResult) {
        final Collector<FieldError, ?, Map<String, String>> collector = Collectors.toMap(
                fieldError -> fieldError.getField() + "Error",
                FieldError::getDefaultMessage
        );
        return bindingResult.getFieldErrors().stream().collect(collector);
    }
}
