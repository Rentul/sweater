package com.example.sweater.controller;

import com.example.sweater.domain.Role;
import com.example.sweater.domain.User;
import com.example.sweater.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * Контроллер пользователей
 */
@Controller
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    /**
     * Конструктор
     *
     * @param userService сервис пользователей
     */
    @Autowired
    public UserController(final UserService userService) {
        this.userService = userService;
    }

    /**
     * Получить страницу со списком зарегистрированных пользователей
     *
     * @param model модель фронта
     * @return страница со списком зарегистрированных пользователей
     */
    @GetMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public String userList(final Model model) {

        model.addAttribute("users", userService.findAll());

        return "userList";
    }

    /**
     * Получить страницу редактирования роли пользователя
     *
     * @param user пользователь
     * @param model модель фронта
     * @return страница редактирования роли пользователя
     */
    @GetMapping("{user}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public String userEditForm(@PathVariable final User user, final Model model) {

        model.addAttribute("user", user);
        model.addAttribute("roles", Role.values());

        return "userEdit";
    }

    /**
     * Сохранить пользователя после редактирования его роли
     *
     * @param username имя пользователя
     * @param form форма с данными пользователя
     * @param user пользователь
     * @return страница редактирования поли пользователя
     */
    @PostMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public String userSave(
            @RequestParam final String username,
            @RequestParam final Map<String, String> form,
            @RequestParam("userId") final User user) {

        userService.saveUser(user, username, form);

        return "redirect:/user";
    }

    /**
     * Получить страницу с профилем пользователя
     *
     * @param model модель фронта
     * @param user пользователь
     * @return страница с профилем пользователя
     */
    @GetMapping("profile")
    public String getProfile(final Model model, @AuthenticationPrincipal final User user) {

        model.addAttribute("username", user.getUsername());
        model.addAttribute("email", user.getEmail());

        return "profile";
    }

    /**
     * Редактировать профиль
     *
     * @param user пользователь
     * @param password пароль
     * @param email электронная почта
     * @return страница с профилем пользователя
     */
    @PostMapping("profile")
    public String updateProfile(
            @AuthenticationPrincipal final User user,
            @RequestParam final String password,
            @RequestParam final String email) {

        userService.updateProfile(user, password, email);

        return "redirect:/user/profile";
    }

    /**
     * Подписаться на пользователя
     *
     * @param currentUser текущий пользователь
     * @param user пользователь, на которого оформляется подписка
     * @return страница с сообщениями пользователя
     */
    @GetMapping("subscribe/{user}")
    public String subscribe(
            @AuthenticationPrincipal final User currentUser,
            @PathVariable final User user) {

        userService.subscribe(currentUser, user);

        return "redirect:/user-messages/" + user.getId();
    }

    /**
     * Отписаться от пользователя
     *
     * @param currentUser текущий пользователь
     * @param user пользователь, от которого отписываются
     * @return страница с сообщениями пользователя
     */
    @GetMapping("unsubscribe/{user}")
    public String unsubscribe(
            @AuthenticationPrincipal final User currentUser,
            @PathVariable final User user) {

        userService.unsubscribe(currentUser, user);

        return "redirect:/user-messages/" + user.getId();
    }

    /**
     * Получить страницу с подписчиками или подписками
     *
     * @param type подписчики или подписки
     * @param user пользователь
     * @param model модель фронта
     * @return страница с подписчиками или подписками
     */
    @GetMapping("{type}/{user}/list")
    public String userList(
            @PathVariable final String type,
            @PathVariable final User user,
            final Model model) {

        model.addAttribute("userChannel", user);
        model.addAttribute("type", type);

        if ("subscriptions".equals(type)) {
            model.addAttribute("users", user.getSubscriptions());
        } else {
            model.addAttribute("users", user.getSubscribers());
        }

        return "subscriptions";
    }

    /**
     * Получить страницу с запросами на подписку
     *
     * @param currentUser пользователь, запросы к которому нужно отобразить
     * @param model модель фронта
     * @return страница с запросами на подписку
     */
    @GetMapping("almost-subscribers-list")
    public String almostSubscribersList(
            @AuthenticationPrincipal final User currentUser,
            final Model model) {

        model.addAttribute("users", userService.getAlmostSubscribers(currentUser));

        return "almostSubscribersList";
    }

    /**
     * Подтверждение подписки
     *
     * @param currentUser пользователь, подтверждающий подписку
     * @param user подписчик
     * @return страница с запросами на подписку
     */
    @GetMapping("accept-subscription/{user}")
    public String acceptSubscription(
            @AuthenticationPrincipal User currentUser,
            @PathVariable User user) {

        userService.acceptSubscription(currentUser, user);

        return "redirect:/user/almost-subscribers-list";
    }
}
