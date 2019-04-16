package com.example.sweater.domain;

import org.springframework.security.core.GrantedAuthority;

/**
 * Роли пользователя
 */
public enum Role implements GrantedAuthority {
    USER, ADMIN, ANALYTIC;

    /**
     * Получить роль пользователя
     *
     * @return роль пользователя
     */
    @Override
    public String getAuthority() {
        return name();
    }
}
