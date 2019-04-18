package com.example.sweater.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

/**
 * Конфигурация аспектов
 */
@Configuration
@EnableAspectJAutoProxy(proxyTargetClass = true)
public class AspectConfig {
}
