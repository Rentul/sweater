package com.example.sweater.config;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Конфигурация логирования
 */
@Configuration
public class LogConfig {

    /**
     * Получить бин логирования
     *
     * @return бин логирования
     */
    @Bean
    public Logger getLogger() {
        return LogManager.getFormatterLogger("logger");
    }
}
