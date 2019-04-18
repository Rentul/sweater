package com.example.sweater.aspect.logging;

import com.example.sweater.domain.User;
import org.apache.logging.log4j.Logger;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Аспект логирования для класса RegistrationService
 */
@Aspect
@Component
public class RegistrationLoggingAspect {

    private final Logger log;

    /**
     * Конструктор
     *
     * @param log логгер
     */
    @Autowired
    public RegistrationLoggingAspect(final Logger log) {
        this.log = log;
    }

    /**
     * Точка среза: метод getCaptchaResponse
     */
    @Pointcut("execution(* com.example.sweater.service.RegistrationService.getCaptchaResponse(..))")
    public void getCaptchaResponse() {}

    /**
     * Точка среза: метод addUser
     */
    @Pointcut("execution(* com.example.sweater.service.RegistrationService.addUser(..))")
    public void addUser() {}

    /**
     * Точка среза: метод activateUser
     */
    @Pointcut("execution(* com.example.sweater.service.RegistrationService.activateUser(..))")
    public void activateUser() {}

    /**
     * Логирование перед выполнением метода, указанного в точке среза getCaptchaResponse
     */
    @Before("getCaptchaResponse()")
    public void beforeGetCaptchaResponse(final JoinPoint joinPoint){

        final Object[] lArgs = joinPoint.getArgs();
        final String clientCaptchaResponse = (String) lArgs[0];

        log.info("Trying to get captcha response from google with client captcha response: {}",
                clientCaptchaResponse);
    }

    /**
     * Логирование после выполнения метода, указанного в точке среза getCaptchaResponse
     */
    @AfterReturning("getCaptchaResponse()")
    public void afterGetCaptchaResponse(final JoinPoint joinPoint){

        final Object[] lArgs = joinPoint.getArgs();
        final String clientCaptchaResponse = (String) lArgs[0];

        log.info("Successfully got captcha response from google with client captcha response: {}",
                clientCaptchaResponse);
    }

    /**
     * Логирование перед выполнением метода, указанного в точке среза addUser
     */
    @Before("addUser()")
    public void beforeAddUser(final JoinPoint joinPoint){

        final Object[] lArgs = joinPoint.getArgs();
        final User user = (User) lArgs[0];

        log.info("Trying to add user {}", user.getUsername());
    }

    /**
     * Логирование после выполнения метода, указанного в точке среза addUser
     */
    @AfterReturning("addUser()")
    public void afterAddUser(final JoinPoint joinPoint){

        final Object[] lArgs = joinPoint.getArgs();
        final User user = (User) lArgs[0];

        log.info("Successfully added user {}", user.getUsername());
    }

    /**
     * Логирование перед выполнением метода, указанного в точке среза activateUser
     */
    @Before("activateUser()")
    public void beforeActivateUser(){

        log.info("Trying to activate user");
    }

    /**
     * Логирование после выполнения метода, указанного в точке среза activateUser
     */
    @AfterReturning("activateUser()")
    public void afterActivateUser(){

        log.info("Successfully activated user");
    }
}
