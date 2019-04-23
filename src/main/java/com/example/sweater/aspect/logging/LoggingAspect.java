package com.example.sweater.aspect.logging;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import java.util.Arrays;

/**
 * Аспект логирования
 */
@Aspect
@Component
public class LoggingAspect {

    private static Logger log = LogManager.getFormatterLogger("logger");

    /**
     * Точка среза: любой public метод
     */
    @Pointcut("execution(public * com.example.sweater..*(..))")
    public void publicMethodExecution() {}

    /**
     * Логирование любого public метода
     */
    @Around("publicMethodExecution()")
    public Object aroundPublicMethodExecution(final ProceedingJoinPoint joinPoint){

        final Object[] lArgs = joinPoint.getArgs();

        Object result = new Object();

        log.info("Вызов метода: " + joinPoint.getSignature().toShortString() +
                "с параметрами: " + Arrays.toString(joinPoint.getArgs()));

        try {
            result = joinPoint.proceed();
        } catch (Throwable throwable) {
            log.error(throwable.getMessage());
        }

        final String returned =  result != null ? result.toString() : "null";

        log.info("Возвращенное значение: " + returned);

        return result;
    }
}
