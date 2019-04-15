package com.example.sweater.service;

import com.example.sweater.domain.Role;
import com.example.sweater.domain.User;
import com.example.sweater.repos.UserRepo;
import com.example.sweater.service.mail.MailSender;
import org.hamcrest.CoreMatchers;
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

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * Юнит-тест сервиса регистрации
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class RegistrationServiceTest {

    @Autowired
    private RegistrationService registrationService;

    @MockBean
    private UserRepo userRepo;

    @MockBean
    private MailSender mailSender;

    @MockBean
    private PasswordEncoder passwordEncoder;

    /**
     * Тест на удачное добавление нового пользователя
     */
    @Test
    public void addUser() {
        User user = new User();

        user.setEmail("some@mail.ru");
        user.setRoles(new HashSet<>());

        Map<String, String> form = new HashMap<>();

        boolean isUserCreated = registrationService.addUser(user);

        assertTrue(isUserCreated);
        assertNotNull(user.getActivationCode());
        assertTrue(CoreMatchers.is(user.getRoles()).matches(Collections.singleton(Role.USER)));

        Mockito.verify(userRepo, Mockito.times(1)).save(user);
        Mockito.verify(mailSender, Mockito.times(1))
                .sendMessage(ArgumentMatchers
                        .eq(user));
    }

    /**
     * Тест на неудачное добавление нового пользователя
     */
    @Test
    public void addUserFailTest() {
        User user = new User();
        user.setUsername("John");
        user.setRoles(new HashSet<>());

        Map<String, String> form = new HashMap<>();

        Mockito.doReturn(new User())
                .when(userRepo)
                .findByUsername("John");

        boolean isUserCreated = registrationService.addUser(user);

        Assert.assertFalse(isUserCreated);

        Mockito.verify(userRepo, Mockito.times(0)).save(ArgumentMatchers.any(User.class));
        Mockito.verify(mailSender, Mockito.times(0))
                .sendMessage(user);
    }

    /**
     * Тест на удачную активацию пользователя
     */
    @Test
    public void activateUser() {
        User user = new User();

        user.setActivationCode("activate");

        Mockito.doReturn(user)
                .when(userRepo)
                .findByActivationCode("activate");

        boolean isUserActivated = registrationService.activateUser(user.getActivationCode());

        Assert.assertTrue(isUserActivated);
        Assert.assertNull(user.getActivationCode());

        Mockito.verify(userRepo, Mockito.times(1)).save(user);
    }

    /**
     * Тест на неудачную активацию пользователя
     */
    @Test
    public void activateUserFailTest() {
        boolean isUserActivated = registrationService.activateUser("activate me");

        Assert.assertFalse(isUserActivated);

        Mockito.verify(userRepo, Mockito.times(0)).save(ArgumentMatchers.any(User.class));
    }
}