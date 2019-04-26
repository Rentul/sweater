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
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.stream.Collectors;

/**
 * {@inheritDoc}
 */
@Service
@Transactional
public class UserServiceImpl implements UserDetailsService, UserService {

    private final UserRepo userRepo;

    private final MailSender mailSender;

    /**
     * Конструктор
     *  @param userRepo репозиторий пользователей
     * @param mailSender отправщик сообщений
     */
    @Autowired
    public UserServiceImpl(final UserRepo userRepo, final MailSender mailSender) {
        this.userRepo = userRepo;
        this.mailSender = mailSender;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(final String username) throws UsernameNotFoundException {

        final User user = userRepo.findByUsername(username);

        if (user == null) {
            throw new UsernameNotFoundException("User not found");
        }

        return user;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public List<User> findAll() {
        return userRepo.findAll();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean saveUser(final User user, final String username, final Map<String, String> form) {

        if (user == null || StringUtils.isEmpty(username)) {
            return false;
        }

        user.setUsername(username);

        final Set<String> roles = Arrays.stream(Role.values()).
                map(Role::name).
                collect(Collectors.toSet());

        user.getRoles().clear();

        for (final String key : form.keySet()) {
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
    public void updateProfile(final User user, final String password, final String email) {

        final String userEmail = user.getEmail();

        boolean isEmailChanged = (email != null && !email.equals(userEmail) && !StringUtils.isEmpty(email));

        if (isEmailChanged) {
            user.setEmail(email);
            user.setActivationCode(UUID.randomUUID().toString());
            user.setActive(false);
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
    public void subscribe(final User currentUser, final User user) {

        user.getAlmostSubscribers().add(currentUser);

        userRepo.save(user);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void unsubscribe(final User currentUser, final User user) {

        user.getSubscribers().remove(currentUser);

        userRepo.save(user);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public List<User> getAlmostSubscribers(User currentUser) {
        return new ArrayList<>(userRepo.findById(currentUser.getId()).get().getAlmostSubscribers());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void acceptSubscription(User currentUser, User user) {
        userRepo.findById(currentUser.getId()).get().getAlmostSubscribers().remove(user);
        userRepo.findById(currentUser.getId()).get().getSubscribers().add(user);
        userRepo.save(currentUser);
    }
}
