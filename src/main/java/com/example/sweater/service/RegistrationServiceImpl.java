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

import java.util.Collections;
import java.util.UUID;

@Service
public class RegistrationServiceImpl implements RegistrationService{

    private UserRepo userRepo;

    private MailSender mailSender;

    private PasswordEncoder passwordEncoder;

    private CaptchaValidator captchaValidator;

    @Autowired
    public RegistrationServiceImpl(UserRepo userRepo, MailSender mailSender, PasswordEncoder passwordEncoder, CaptchaValidator captchaValidator) {
        this.userRepo = userRepo;
        this.mailSender = mailSender;
        this.passwordEncoder = passwordEncoder;
        this.captchaValidator = captchaValidator;
    }

    public CaptchaResponseDto getCaptchaResponse(String clientCaptchaResponse) {
        return captchaValidator.validate(clientCaptchaResponse);
    }

    public boolean addUser(User user) {

        User userFromDb = userRepo.findByUsername(user.getUsername());

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

    public boolean activateUser(String code) {

        User user = userRepo.findByActivationCode(code);

        if (user == null) {
            return false;
        }

        user.setActivationCode(null);

        user.setActive(true);

        userRepo.save(user);

        return true;
    }
}
