package com.example.sweater.service;

import com.example.sweater.domain.User;
import com.example.sweater.repos.UserRepo;
import com.example.sweater.service.mail.MailSender;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.*;

/**
 * Юнит-тест сервиса пользователей
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class UserServiceTest {

    @Autowired
    private UserService userService;

    @MockBean
    private UserRepo userRepo;

    /**
     * Тест на нахождение всех пользователей в базе
     */
    @Test
    public void findAllTest() {

        List<User> users = new ArrayList<>();

        Mockito.doReturn(users)
                .when(userRepo)
                .findAll();

        Assert.assertNotNull(userService.findAll());

        Mockito.verify(userRepo, Mockito.times(1)).findAll();
    }

    /**
     * Тест на сохранение пользователя после изменения его ролей
     */
    @Test
    public void saveUserTest() {
        User user = new User();
        user.setUsername("John");
        user.setRoles(new HashSet<>());

        Map<String, String> form = new HashMap<>();

        Assert.assertTrue(userService.saveUser(user, user.getUsername(), form));

        Mockito.verify(userRepo, Mockito.times(1)).save(ArgumentMatchers.any(User.class));

    }

    /**
     * Тест на неудачное сохранение пользователя после изменения его ролей
     */
    @Test
    public void saveUserFailTest() {

        Assert.assertFalse(userService.saveUser(null, null, null));

        Mockito.verify(userRepo, Mockito.times(0)).save(ArgumentMatchers.any(User.class));

    }

    /**
     * Тест на обновление данных пользователя
     */
    @Test
    public void updateProfileTest() {
        User user = new User();

        String password = "password";

        String email = "e@mail.com";

        userService.updateProfile(user, password, email);

        Mockito.verify(userRepo, Mockito.times(1)).save(user);
    }

    /**
     * Тест на подписку пользователя
     */
    @Test
    public void subscribeTest() {
        User user = new User();

        User currentUser = new User();

        userService.subscribe(currentUser, user);

        Mockito.verify(userRepo, Mockito.times(1)).save(user);
    }

    /**
     * Тест на отписку пользователя
     */
    @Test
    public void unsubscribeTest() {
        User user = new User();

        User currentUser = new User();

        userService.unsubscribe(currentUser, user);

        Mockito.verify(userRepo, Mockito.times(1)).save(user);
    }

    /**
     * Тест на получение списка неподтвержденных подписок
     */
    @Test
    public void getAlmostSubscribersTest() {
        User user = new User();

        Mockito.doReturn(Optional.of(user))
                .when(userRepo)
                .findById(user.getId());

        Assert.assertNotNull(userService.getAlmostSubscribers(user));

        Mockito.verify(userRepo, Mockito.times(1)).findById(user.getId());
    }

    /**
     * Тест на подтверждение подписки
     */
    @Test
    public void acceptSubscriptionTest() {
        User user = new User();
        user.setId(1);

        User currentUser = new User();
        currentUser.setId(2);

        Mockito.doReturn(Optional.of(user))
                .when(userRepo)
                .findById(user.getId());

        Mockito.doReturn(Optional.of(currentUser))
                .when(userRepo)
                .findById(currentUser.getId());


        userService.acceptSubscription(currentUser, user);

        Mockito.verify(userRepo, Mockito.times(1)).save(currentUser);
    }
}