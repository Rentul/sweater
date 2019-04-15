package com.example.sweater.repos;

import com.example.sweater.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Репозиторий пользователей
 */
public interface UserRepo extends JpaRepository<User, Integer> {

    /**
     * Найти пользователя по имени
     *
     * @param username имя пользователя
     * @return пользователь
     */
    User findByUsername(String username);

    /**
     * Найти пользователя по коду активации
     *
     * @param code код активации
     * @return пользователь
     */
    User findByActivationCode(String code);
}
