package com.example.sweater.repos;

import com.example.sweater.domain.Message;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

/**
 * Репозиторий сообщений
 */
public interface MessageRepo extends CrudRepository<Message, Integer> {

    /**
     * Найти сообщение по тегу
     *
     * @param tag тег
     * @return сообщение
     */
    List<Message> findByTag(String tag);

}