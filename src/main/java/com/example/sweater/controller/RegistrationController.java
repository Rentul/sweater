package com.example.sweater.controller;

import com.example.sweater.controller.utils.ControllerUtils;
import com.example.sweater.domain.User;
import com.example.sweater.domain.dto.CaptchaResponseDto;
import com.example.sweater.service.RegistrationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.Valid;
import java.util.Map;

/**
 * Контроллер регистрации пользователей
 */
@Controller
public class RegistrationController {

    private final RegistrationService registrationService;

    /**
     * Конструктор
     *
     * @param registrationService сервис регистрации
     */
    @Autowired
    public RegistrationController(final RegistrationService registrationService) {
        this.registrationService = registrationService;
    }

    /**
     * Получить страницу регистрации
     *
     * @return страница регистрации
     */
    @GetMapping("/registration")
    public String registration() {
        return "registration";
    }

    /**
     * Добавить пользователя
     *
     * @param passwordConfirm строка с подтверждением пароля
     * @param clientCaptchaResponse результаты прохождения капчи
     * @param user пользователь
     * @param bindingResult результаты связки
     * @param model модель фронта
     * @return страница входа
     */
    @PostMapping("/registration")
    public String addUser(
            @RequestParam("password2") final String passwordConfirm,
            @RequestParam("g-recaptcha-response") final String clientCaptchaResponse,
            @Valid final User user,
            final BindingResult bindingResult,
            final Model model) {

        final CaptchaResponseDto response = registrationService.getCaptchaResponse(clientCaptchaResponse);

        if (!response.isSuccess()) {
            model.addAttribute("captchaError", "Fill captcha");
        }

        boolean isConfirmEmpty = StringUtils.isEmpty(passwordConfirm);
        if (isConfirmEmpty) {
            model.addAttribute("password2Error", "Password confirmation can not be empty");
        }

        if (user.getPassword() != null && !user.getPassword().equals(passwordConfirm)) {
            model.addAttribute("passwordError", "Passwords are different");
        }

        if (isConfirmEmpty || bindingResult.hasErrors() || !response.isSuccess()) {
            Map<String, String> errors = ControllerUtils.getErrors(bindingResult);
            model.mergeAttributes(errors);
            return "registration";
        }

        if (!registrationService.addUser(user)) {
            model.addAttribute("usernameError", "User exists");
            return "registration";
        }

        model.addAttribute("message", "");

        return "redirect:/login";
    }

    /**
     * Активировать пользователя
     *
     * @param model модель фронта
     * @param code код активации
     * @return страница входа
     */
    @GetMapping("/activate/{code}")
    public String activate(final Model model, @PathVariable final String code) {

        boolean isActivated = registrationService.activateUser(code);

        if (isActivated) {
            model.addAttribute("messageType", "success");
            model.addAttribute("message", "User successfully activated");
        } else {
            model.addAttribute("messageType", "danger");
            model.addAttribute("message", "Activation code is not found!");
        }

        return "login";
    }
}
