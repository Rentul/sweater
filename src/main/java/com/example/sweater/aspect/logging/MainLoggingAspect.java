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

/**
 * Аспект логирования для класса MainService
 */
@Aspect
@Component
public class MainLoggingAspect {

    private final Logger log;

    /**
     * Конструктор
     *
     * @param log логгер
     */
    @Autowired
    public MainLoggingAspect(final Logger log) {
        this.log = log;
    }

    /**
     * Точка среза: метод
     */
    @Pointcut("execution(* com.example.sweater.service.MainService.saveMessage(..))")
    public void saveMessage() {}

    /**
     * Точка среза: метод
     */
    @Pointcut("execution(* com.example.sweater.service.MainService.deleteMessage(..))")
    public void deleteMessage() {}

    /**
     * Точка среза: метод
     */
    @Pointcut("execution(* com.example.sweater.service.MainService.updateMessage(..))")
    public void updateMessage() {}

    /**
     * Точка среза: метод
     */
    @Pointcut("execution(* com.example.sweater.service.MainService.downloadFile(..))")
    public void downloadFile() {}

    /**
     * Точка среза: метод
     */
    @Pointcut("execution(* com.example.sweater.service.MainService.getFilteredMessages(..))")
    public void getFilteredMessages() {}

    /**
     * Точка среза: метод
     */
    @Pointcut("execution(* com.example.sweater.service.MainService.findAllUsers(..))")
    public void findAllUsers() {}

    /**
     * Точка среза: метод
     */
    @Pointcut("execution(* com.example.sweater.service.MainService.getAnalytics(..))")
    public void getAnalytics() {}

    /**
     * Точка среза: метод
     */
    @Pointcut("execution(* com.example.sweater.service.MainService.getTotalsForAnalytics(..))")
    public void getTotalsForAnalytics() {}

    /**
     * Точка среза: метод
     */
    @Pointcut("execution(* com.example.sweater.service.MainService.getUserAnalyticsByFiles(..))")
    public void getUserAnalyticsByFiles() {}

    /**
     * Логирование перед выполнением метода, указанного в точке среза
     */
    @Before("saveMessage()")
    public void beforeSaveMessage(final JoinPoint joinPoint){

        final Object[] lArgs = joinPoint.getArgs();
        final User user = (User) lArgs[0];
        final Message message = (Message) lArgs[1];
        final MultipartFile file = (MultipartFile) lArgs[2];

        if (!file.isEmpty()) {
            log.info("Trying to add message with tag {} and file named {} to user {} with id {}",
                    message.getTag(), file.getOriginalFilename(), user.getUsername(), user.getId());
        } else {
            log.info("Trying to add message with tag {} without any file to user {} with id {}",
                    message.getTag(), user.getUsername(), user.getId());
        }
    }

    @AfterReturning("saveMessage()")
    public void afterSaveMessage(final JoinPoint joinPoint){

        final Object[] lArgs = joinPoint.getArgs();
        final User user = (User) lArgs[0];
        final Message message = (Message) lArgs[1];
        final MultipartFile file = (MultipartFile) lArgs[2];

        if (!file.isEmpty()) {
            log.info("Successfully added message with tag {} and file named {} to user {} with id {}",
                    message.getTag(), file.getOriginalFilename(), user.getUsername(), user.getId());
        } else {
            log.info("Successfully added message with tag {} without any file to user {} with id {}",
                    message.getTag(), user.getUsername(), user.getId());
        }
    }

    /**
     * Логирование перед выполнением метода, указанного в точке среза
     */
    @Before("deleteMessage()")
    public void beforeDeleteMessage(final JoinPoint joinPoint){

        final Object[] lArgs = joinPoint.getArgs();
        final Message message = (Message) lArgs[0];

        log.info("Trying to delete message with id {} from user {} with id {}",
                message.getId(), message.getAuthorName(), message.getAuthor().getId());
    }

    @AfterReturning("deleteMessage()")
    public void afterDeleteMessage(final JoinPoint joinPoint){

        final Object[] lArgs = joinPoint.getArgs();
        final Message message = (Message) lArgs[0];

        log.info("Successfully deleted message with id {} from user {} with id {}",
                message.getId(), message.getAuthorName(), message.getAuthor().getId());
    }

    /**
     * Логирование перед выполнением метода, указанного в точке среза
     */
    @Before("updateMessage()")
    public void beforeUpdateMessage(final JoinPoint joinPoint){

        final Object[] lArgs = joinPoint.getArgs();
        final User currentUser = (User) lArgs[0];
        final Message message = (Message) lArgs[1];

        log.info("User {} with id {} is trying to update message with id {} from user {} with id {}",
                currentUser.getUsername(), currentUser.getId(), message.getId(), message.getAuthorName(), message.getAuthor().getId());
    }

    @AfterReturning("updateMessage()")
    public void afterUpdateMessage(final JoinPoint joinPoint) {

        final Object[] lArgs = joinPoint.getArgs();
        final User currentUser = (User) lArgs[0];
        final Message message = (Message) lArgs[1];

        log.info("User {} with id {} successfully updated message with id {} from user {} with id {}",
                currentUser.getUsername(), currentUser.getId(), message.getId(), message.getAuthorName(), message.getAuthor().getId());
    }

    /**
     * Логирование перед выполнением метода, указанного в точке среза
     */
    @Before("downloadFile()")
    public void beforeDownloadFile(final JoinPoint joinPoint){

        final Object[] lArgs = joinPoint.getArgs();
        final Message message = (Message) lArgs[0];

        log.info("Trying to download file {} from message with id {}",
                message.getFilename(), message.getId());
    }

    @AfterReturning("downloadFile()")
    public void afterDownloadFile(final JoinPoint joinPoint) {

        final Object[] lArgs = joinPoint.getArgs();
        final Message message = (Message) lArgs[0];

        log.info("Successfully downloaded file {} from message with id {}",
                message.getFilename(), message.getId());
    }

    /**
     * Логирование перед выполнением метода, указанного в точке среза
     */
    @Before("getFilteredMessages()")
    public void beforeGetFilteredMessages(final JoinPoint joinPoint){

        final Object[] lArgs = joinPoint.getArgs();
        final String filter = (String) lArgs[0];

        log.info("Trying to get filtered messages by filter {}",
                filter);
    }

    @AfterReturning("getFilteredMessages()")
    public void afterGetFilteredMessages(final JoinPoint joinPoint) {

        final Object[] lArgs = joinPoint.getArgs();
        final String filter = (String) lArgs[0];

        log.info("Successfully got filtered messages by filter {}",
                filter);
    }

    /**
     * Логирование перед выполнением метода, указанного в точке среза findAllUsers
     */
    @Before("findAllUsers()")
    public void beforeFindAllUsers(){

        log.info("Trying to find all users");
    }

    /**
     * Логирование после выполнения метода, указанного в точке среза findAllUsers
     */
    @AfterReturning("findAllUsers()")
    public void afterFindAllUsers() {

        log.info("Successfully found all users");
    }

    /**
     * Логирование перед выполнением метода, указанного в точке среза getAnalytics
     */
    @Before("getAnalytics()")
    public void beforeGetAnalytics(){

        log.info("Trying to get analytics");
    }

    /**
     * Логирование после выполнения метода, указанного в точке среза getAnalytics
     */
    @AfterReturning("getAnalytics()")
    public void afterGetAnalytics() {

        log.info("Successfully got analytics");
    }

    /**
     * Логирование перед выполнением метода, указанного в точке среза getTotalsForAnalytics
     */
    @Before("getTotalsForAnalytics()")
    public void beforeGetTotalsForAnalytics(){

        log.info("Trying to get totals for analytics");
    }

    /**
     * Логирование после выполнения метода, указанного в точке среза getTotalsForAnalytics
     */
    @AfterReturning("getTotalsForAnalytics()")
    public void afterGetTotalsForAnalytics() {

        log.info("Successfully got totals for analytics");
    }

    /**
     * Логирование перед выполнением метода, указанного в точке среза getUserAnalyticsByFiles
     */
    @Before("getUserAnalyticsByFiles()")
    public void beforeGetUserAnalyticsByFiles(final JoinPoint joinPoint) {

        final Object[] lArgs = joinPoint.getArgs();
        final User user = (User) lArgs[0];

        log.info("Trying to get analytics about user {} with id {}",
                user.getUsername(), user.getId());
    }

    /**
     * Логирование после выполнения метода, указанного в точке среза getUserAnalyticsByFiles
     */
    @AfterReturning("getUserAnalyticsByFiles()")
    public void afterGetUserAnalyticsByFiles(final JoinPoint joinPoint) {

        final Object[] lArgs = joinPoint.getArgs();
        final User user = (User) lArgs[0];

        log.info("Successfully got analytics about user {} with id {}",
                user.getUsername(), user.getId());
    }
}
