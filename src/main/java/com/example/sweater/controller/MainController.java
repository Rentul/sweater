package com.example.sweater.controller;

import com.example.sweater.controller.utils.ControllerUtils;
import com.example.sweater.domain.Message;
import com.example.sweater.domain.User;
import com.example.sweater.service.MainService;
import com.example.sweater.view.analytics.UserAnalyticsView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Основной контроллер
 */
@Controller
public class MainController {

    private MainService mainService;

    /**
     * Конструктор
     *
     * @param mainService основной сервис
     */
    @Autowired
    public MainController(MainService mainService) {
        this.mainService = mainService;
    }

    /**
     * Получить страницу приветствия
     *
     * @return страница приветствия
     */
    @GetMapping("/")
    public String greeting() {
        return "greeting";
    }

    /**
     * Получить главную страницу
     *
     * @param filter фильтрация по сообщениям
     * @param model модель фронта
     * @return главная страница
     */
    @GetMapping("/main")
    public String main(@RequestParam(required = false, defaultValue = "") String filter, Model model) {

        Iterable<Message> messages = mainService.getFilteredMessages(filter);

        model.addAttribute("messages", messages);
        model.addAttribute("filter", filter);

        return "main";
    }

    /**
     * Добавить сообщение
     *
     * @param user пользователь
     * @param message сообщение
     * @param bindingResult результаты связки
     * @param model модель фронта
     * @param file файл
     * @return главная страница
     * @throws IOException
     */
    @PostMapping("/main")
    public String add(
            @AuthenticationPrincipal User user,
            @Valid Message message,
            BindingResult bindingResult,
            Model model,
            @RequestParam("file") MultipartFile file
    ) throws IOException {

        if (bindingResult.hasErrors()) {

            Map<String, String> errorsMap = ControllerUtils.getErrors(bindingResult);

            model.mergeAttributes(errorsMap);
            model.addAttribute("message", message);
        } else {

            mainService.saveMessage(user, message, file);

            model.addAttribute("message", null);
        }

        Iterable<Message> messages = mainService.getFilteredMessages("");

        model.addAttribute("messages", messages);

        return "main";
    }

    /**
     * Получить страницу пользователя
     *
     * @param currentUser текущий пользователь
     * @param user пользователь, чью страницу нужно отобразить
     * @param model модель фронта
     * @return страница полььзователя
     */
    @GetMapping("/user-messages/{user}")
    public String userMessages(
            @AuthenticationPrincipal User currentUser,
            @PathVariable User user,
            Model model) {

        Set<Message> messages = user.getMessages();

        model.addAttribute("userChannel", user);
        model.addAttribute("subscribersCount", user.getSubscribers().size());
        model.addAttribute("subscriptionsCount", user.getSubscribtions().size());
        model.addAttribute("messages", messages);
        model.addAttribute("isCurrentUser", currentUser.equals(user));
        model.addAttribute("isSubscriber", user.getSubscribtions().contains(currentUser));

        return "userMessages";
    }

    /**
     * Получить страницу редактирования сообщения
     *
     * @param currentUser текущий пользователь
     * @param message сообщение
     * @param user пользователь, владеющий сообщением
     * @param model модель фронта
     * @return страница редактирования сообщения
     */
    @GetMapping("/user-message-edit/{user}")
    public String userMessageEdit(
            @AuthenticationPrincipal User currentUser,
            @RequestParam Message message,
            @PathVariable User user,
            Model model) {

        model.addAttribute("userChannel", user);
        model.addAttribute("message", message);
        model.addAttribute("isCurrentUser", currentUser.equals(user));

        return "/messageEdit";
    }

    /**
     * Отправить форму редактированного сообщения
     *
     * @param currentUser текущий пользователь
     * @param user владелец сообщения
     * @param message сообщение
     * @param text текст сообщения
     * @param tag тег
     * @param file файл
     * @return страница сообщений пользователя
     * @throws IOException
     */
    @PostMapping("/user-message-edit/{user}")
    public String updateMessage(
            @AuthenticationPrincipal User currentUser,
            @PathVariable Integer user,
            @RequestParam("id") Message message,
            @RequestParam("text") String text,
            @RequestParam("tag") String tag,
            @RequestParam("file") MultipartFile file
    ) throws IOException {

        mainService.updateMessage(currentUser, message, text, tag, file);

        return "redirect:/user-messages/" + user;
    }


    /**
     * Загрузить файл с сервера
     *
     * @param message сообщение, к которому прикреплен файл
     * @param response ответ сервера, содержащий файл
     * @throws Exception
     */
    @GetMapping("/download-file/{user}")
    public void downloadFile(
            @RequestParam Message message,
            HttpServletResponse response) throws Exception {

        response.setContentType("application/force-download");
        response.setHeader("Content-Disposition", "attachment; filename=" + message.getFilename());

        mainService.downloadFile(message, response);
    }

    /**
     * Удалить сообщение
     *
     * @param user пользователь
     * @param message сообщение
     * @return страница сообщений пользователя
     */
    @GetMapping("/delete-message/{user}")
    public String deleteMessage(
            @PathVariable Integer user,
            @RequestParam Message message) {

        mainService.deleteMessage(message);

        return "redirect:/user-messages/" + user;
    }

    /**
     * Получить аналитику
     *
     * @param model модель фронта
     * @return страница с аналитикой
     */
    @GetMapping("/analytics")
    @PreAuthorize("hasAuthority('ANALYTIC')")
    public String getAnalytics(Model model) {

        List<UserAnalyticsView> analytics = mainService.getAnalytics();

        model.addAttribute("userAnalyticsList", analytics);
        model.addAttribute("userAnalyticsTotal", mainService.getTotalsForAnalytics(analytics));

        return "userListAnalytics";
    }

    /**
     * Получить аналитику выбранного пользователя
     *
     * @param user пользователь
     * @param model модель фронта
     * @return страница с аналитикой пользователя
     */
    @GetMapping("/user-analytics/{user}")
    @PreAuthorize("hasAuthority('ANALYTIC')")
    public String getAnalytics(@PathVariable User user, Model model) {

        model.addAttribute("userAnalyticsList", mainService.getUserAnalyticsByFiles(user));

        return "userAnalytics";
    }
}