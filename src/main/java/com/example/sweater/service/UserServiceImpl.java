package com.example.sweater.service;

import com.example.sweater.domain.Role;
import com.example.sweater.domain.User;
import com.example.sweater.repos.UserRepo;
import com.example.sweater.service.mail.MailSender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.stream.Collectors;

/**
 * {@inheritDoc}
 */
@Service
public class UserServiceImpl implements UserDetailsService, UserService {

    private UserRepo userRepo;

    private MailSender mailSender;

    /**
     * Конструктор
     *
     * @param userRepo репозиторий пользователей
     * @param mailSender отправщик сообщений
     */
    @Autowired
    public UserServiceImpl(UserRepo userRepo, MailSender mailSender) {
        this.userRepo = userRepo;
        this.mailSender = mailSender;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        User user = userRepo.findByUsername(username);

        if (user == null) {
            throw new UsernameNotFoundException("User not found");
        }

        return user;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<User> findAll() {
        return userRepo.findAll();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean saveUser(User user, String username, Map<String, String> form) {

        if (user == null || StringUtils.isEmpty(username)) {
            return false;
        }

        user.setUsername(username);

        Set<String> roles = Arrays.stream(Role.values()).
                map(Role::name).
                collect(Collectors.toSet());

        user.getRoles().clear();

        for (String key : form.keySet()) {
            if (roles.contains(key)) {
                user.getRoles().add(Role.valueOf(key));
            }
        }

        userRepo.save(user);

        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void updateProfile(User user, String password, String email) {

        String userEmail = user.getEmail();

        boolean isEmailChanged = (email != null && !email.equals(userEmail)) || (userEmail != null && !userEmail.equals(email));

        if (isEmailChanged) {
            user.setEmail(email);

            if (StringUtils.isEmpty(email)) {
                user.setActivationCode(UUID.randomUUID().toString());
            }
        }

        if (!StringUtils.isEmpty(password)) {
            user.setPassword(password);
        }

        userRepo.save(user);

        if (isEmailChanged) {
            mailSender.sendMessage(user);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void subscribe(User currentUser, User user) {

        user.getSubscribers().add(currentUser);

        userRepo.save(user);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void unsubscribe(User currentUser, User user) {

        user.getSubscribers().remove(currentUser);

        userRepo.save(user);
    }
}
