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

    private final MainService mainService;

    /**
     * Конструктор
     *
     * @param mainService основной сервис
     */
    @Autowired
    public MainController(final MainService mainService) {
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
    public String main(@RequestParam(required = false, defaultValue = "") final String filter, final Model model) {

        final Iterable<Message> messages = mainService.getFilteredMessages(filter);

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
            @AuthenticationPrincipal final User user,
            @Valid final Message message,
            final BindingResult bindingResult,
            final Model model,
            @RequestParam("file") final MultipartFile file
    ) throws IOException {

        if (bindingResult.hasErrors()) {

            final Map<String, String> errorsMap = ControllerUtils.getErrors(bindingResult);

            model.mergeAttributes(errorsMap);
            model.addAttribute("message", message);

        } else {

            mainService.saveMessage(user, message, file);

            model.addAttribute("message", null);
        }

        final Iterable<Message> messages = mainService.getFilteredMessages("");

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
            @AuthenticationPrincipal final User currentUser,
            @PathVariable final User user,
            final Model model) {

        final Set<Message> messages = user.getMessages();

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
            @AuthenticationPrincipal final User currentUser,
            @RequestParam final Message message,
            @PathVariable final User user,
            final Model model) {

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
            @AuthenticationPrincipal final User currentUser,
            @PathVariable final Integer user,
            @RequestParam("id") final Message message,
            @RequestParam("text") final String text,
            @RequestParam("tag") final String tag,
            @RequestParam("file") final MultipartFile file
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
            @RequestParam final Message message,
            final HttpServletResponse response) throws Exception {

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
            @PathVariable final Integer user,
            @RequestParam final Message message) {

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
    public String getAnalytics(final Model model) {

        final List<UserAnalyticsView> analytics = mainService.getAnalytics();

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
    public String getAnalytics(@PathVariable final User user, final Model model) {

        model.addAttribute("userAnalyticsList", mainService.getUserAnalyticsByFiles(user));

        return "userAnalytics";
    }
}