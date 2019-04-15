package com.example.sweater.domain.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.Set;

/**
 * Ответ google на капчу
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class CaptchaResponseDto {

    private boolean success;

    @JsonAlias("error-codes")
    private Set<String> errorCodes;

    /**
     * Успешно ли пройдена капча
     *
     * @return true/false
     */
    public boolean isSuccess() {
        return success;
    }

    /**
     * Сеттер поля success
     *
     * @param success
     */
    public void setSuccess(boolean success) {
        this.success = success;
    }

    /**
     * Геттер кодов ошибок
     *
     * @return сет кодов ошибок
     */
    public Set<String> getErrorCodes() {
        return errorCodes;
    }

    /**
     * Сеттер кодов ошибок
     *
     * @param errorCodes сет кодов ошибок
     */
    public void setErrorCodes(Set<String> errorCodes) {
        this.errorCodes = errorCodes;
    }
}
