package com.example.sweater.service;

import com.example.sweater.domain.Role;
import com.example.sweater.domain.User;
import com.example.sweater.domain.dto.CaptchaResponseDto;
import com.example.sweater.repos.UserRepo;
import com.example.sweater.service.captcha.CaptchaValidator;
import com.example.sweater.service.mail.MailSender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.UUID;

/**
 * {@inheritDoc}
 */
@Service
public class RegistrationServiceImpl implements RegistrationService{

    private final UserRepo userRepo;

    private final MailSender mailSender;

    private final PasswordEncoder passwordEncoder;

    private final CaptchaValidator captchaValidator;

    /**
     * Конструктор
     *
     * @param userRepo репозиторий пользователей
     * @param mailSender отправщик сообщений
     * @param passwordEncoder кодировщик пароля
     * @param captchaValidator валидатор капчи
     */
    @Autowired
    public RegistrationServiceImpl(final UserRepo userRepo,
                                   final MailSender mailSender,
                                   final PasswordEncoder passwordEncoder,
                                   final CaptchaValidator captchaValidator) {
        this.userRepo = userRepo;
        this.mailSender = mailSender;
        this.passwordEncoder = passwordEncoder;
        this.captchaValidator = captchaValidator;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public CaptchaResponseDto getCaptchaResponse(final String clientCaptchaResponse) {
        return captchaValidator.validate(clientCaptchaResponse);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public boolean addUser(final User user) {

        final User userFromDb = userRepo.findByUsername(user.getUsername());

        if (userFromDb != null) {
            return false;
        }

        user.setActive(false);
        user.setRoles(Collections.singleton(Role.USER));
        user.setActivationCode(UUID.randomUUID().toString());
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        userRepo.save(user);

        mailSender.sendMessage(user);

        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public boolean activateUser(final String code) {

        final User user = userRepo.findByActivationCode(code);

        if (user == null) {
            return false;
        }

        user.setActivationCode(null);

        user.setActive(true);

        userRepo.save(user);

        return true;
    }
}
