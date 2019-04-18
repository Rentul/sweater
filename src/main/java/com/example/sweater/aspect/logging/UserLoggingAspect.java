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

@Aspect
@Component
public class UserLoggingAspect {

    private final Logger log;

    @Autowired
    public UserLoggingAspect(final Logger log) {
        this.log = log;
    }

    @Pointcut("execution(* com.example.sweater.service.UserService.findAll(..))")
    public void findAll() {}

    @Pointcut("execution(* com.example.sweater.service.UserService.saveUser(..))")
    public void saveUser() {}

    @Pointcut("execution(* com.example.sweater.service.UserService.updateProfile(..))")
    public void updateProfile() {}

    @Pointcut("execution(* com.example.sweater.service.UserService.subscribe(..))")
    public void subscribe() {}

    @Pointcut("execution(* com.example.sweater.service.UserService.unsubscribe(..))")
    public void unsubscribe() {}

    @Pointcut("execution(* com.example.sweater.service.UserService.getAlmostSubscribers(..))")
    public void getAlmostSubscribers() {}

    @Pointcut("execution(* com.example.sweater.service.UserService.findAll(..))")
    public void acceptSubscription() {}

    @Before("findAll()")
    public void beforeFindAll(){

        log.info("Trying find all users");
    }

    @AfterReturning("findAll()")
    public void afterFindAll(){

        log.info("Successfully found all users");
    }

    @Before("saveUser()")
    public void beforeSaveUser(final JoinPoint joinPoint){

        final Object[] lArgs = joinPoint.getArgs();
        final String username = (String) lArgs[1];

        log.info("Trying to save user with name {}", username);
    }

    @AfterReturning("saveUser()")
    public void afterSaveUser(final JoinPoint joinPoint){

        final Object[] lArgs = joinPoint.getArgs();
        final String username = (String) lArgs[1];

        log.info("Successfully saved user with username {}",
                username);
    }

    @Before("updateProfile()")
    public void beforeUpdateProfile(final JoinPoint joinPoint){

        final Object[] lArgs = joinPoint.getArgs();
        final User user = (User) lArgs[0];
        final String password = (String) lArgs[1];
        final String email = (String) lArgs[2];

        log.info("Trying to save user {} with password {} and email {}",
                user.getUsername(), password, email);
    }

    @AfterReturning("updateProfile()")
    public void afterUpdateProfile(final JoinPoint joinPoint){

        final Object[] lArgs = joinPoint.getArgs();
        final User user = (User) lArgs[0];
        final String password = (String) lArgs[1];
        final String email = (String) lArgs[2];

        log.info("Successfully saved user {} with password {} and email {}",
                user.getUsername(), password, email);
    }

    @Before("subscribe()")
    public void beforeSubscribe(final JoinPoint joinPoint){

        final Object[] lArgs = joinPoint.getArgs();
        final User currentUser = (User) lArgs[0];
        final User user = (User) lArgs[1];

        log.info("Trying to subscribe user {} to user {}",
                currentUser.getUsername(), user.getUsername());
    }

    @AfterReturning("subscribe()")
    public void afterSubscribe(final JoinPoint joinPoint){

        final Object[] lArgs = joinPoint.getArgs();
        final User currentUser = (User) lArgs[0];
        final User user = (User) lArgs[1];

        log.info("Successfully subscribed user {} to user {}",
                currentUser.getUsername(), user.getUsername());
    }

    @Before("unsubscribe()")
    public void beforeUnsubscribe(final JoinPoint joinPoint){

        final Object[] lArgs = joinPoint.getArgs();
        final User currentUser = (User) lArgs[0];
        final User user = (User) lArgs[1];

        log.info("Trying to unsubscribe user {} from user {}",
                currentUser.getUsername(), user.getUsername());
    }

    @AfterReturning("unsubscribe()")
    public void afterUnsubscribe(final JoinPoint joinPoint){

        final Object[] lArgs = joinPoint.getArgs();
        final User currentUser = (User) lArgs[0];
        final User user = (User) lArgs[1];

        log.info("Successfully unsubscribed user {} from user {}",
                currentUser.getUsername(), user.getUsername());
    }

    @Before("getAlmostSubscribers()")
    public void beforeGetAlmostSubscribers(final JoinPoint joinPoint){

        final Object[] lArgs = joinPoint.getArgs();
        final User currentUser = (User) lArgs[0];

        log.info("Trying to get almost subscribers to user {}",
                currentUser.getUsername());
    }

    @AfterReturning("getAlmostSubscribers()")
    public void afterGetAlmostSubscribers(final JoinPoint joinPoint){

        final Object[] lArgs = joinPoint.getArgs();
        final User currentUser = (User) lArgs[0];

        log.info("Successfully got almost subscribers to user {}",
                currentUser.getUsername());
    }

    @Before("acceptSubscription()")
    public void beforeAcceptSubscription(final JoinPoint joinPoint){

        final Object[] lArgs = joinPoint.getArgs();
        final User currentUser = (User) lArgs[0];
        final User user = (User) lArgs[1];

        log.info("User {} tries to accept subscription from user {}",
                currentUser.getUsername(), user.getUsername());
    }

    @AfterReturning("acceptSubscription()")
    public void afterAcceptSubscription(final JoinPoint joinPoint){

        final Object[] lArgs = joinPoint.getArgs();
        final User currentUser = (User) lArgs[0];
        final User user = (User) lArgs[1];

        log.info("User {} successfully accepted subscription from user {}",
                currentUser.getUsername(), user.getUsername());
    }
}
