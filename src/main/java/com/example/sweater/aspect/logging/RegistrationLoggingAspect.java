package com.example.sweater.aspect.logging;

import com.example.sweater.domain.Message;
import com.example.sweater.domain.User;
import org.apache.logging.log4j.Logger;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Aspect
@Component
public class RegistrationLoggingAspect {

    private final Logger log;

    @Autowired
    public RegistrationLoggingAspect(final Logger log) {
        this.log = log;
    }

    @Pointcut("execution(* com.example.sweater.service.RegistrationService.getCaptchaResponse(..))")
    public void getCaptchaResponse() {}

    @Pointcut("execution(* com.example.sweater.service.RegistrationService.addUser(..))")
    public void addUser() {}

    @Pointcut("execution(* com.example.sweater.service.RegistrationService.activateUser(..))")
    public void activateUser() {}

    @Before("getCaptchaResponse()")
    public void beforeGetCaptchaResponse(final JoinPoint joinPoint){

        final Object[] lArgs = joinPoint.getArgs();
        final String clientCaptchaResponse = (String) lArgs[0];

        log.info("Trying to get captcha response from google with client captcha response: ",
                clientCaptchaResponse);
    }

    @AfterReturning("getCaptchaResponse()")
    public void afterGetCaptchaResponse(final JoinPoint joinPoint){

        final Object[] lArgs = joinPoint.getArgs();
        final String clientCaptchaResponse = (String) lArgs[0];

        log.info("Successfully got captcha response from google with client captcha response: ",
                clientCaptchaResponse);
    }

    @Before("addUser()")
    public void beforeAddUser(final JoinPoint joinPoint){

        final Object[] lArgs = joinPoint.getArgs();
        final User user = (User) lArgs[0];

        log.info("Trying to add user {}", user.getUsername());
    }

    @AfterReturning("addUser()")
    public void afterAddUser(final JoinPoint joinPoint){

        final Object[] lArgs = joinPoint.getArgs();
        final User user = (User) lArgs[0];

        log.info("Successfully added user {}", user.getUsername());
    }

    /*boolean activateUser(String code);*/

    @Before("activateUser()")
    public void beforeActivateUser(){

        log.info("Trying to activate user");
    }

    @AfterReturning("activateUser()")
    public void afterActivateUser(){

        log.info("Successfully activated user");
    }
}
