package com.example.sweater.service;

import com.example.sweater.domain.User;

import java.util.List;
import java.util.Map;

public interface UserService {

    List<User> findAll();

    boolean saveUser(User user, String username, Map<String, String> form);

    void updateProfile(User user, String password, String email);

    void subscribe(User currentUser, User user);

    void unsubscribe(User currentUser, User user);
}
