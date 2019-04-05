package com.example.sweater.controller;

import com.example.sweater.controller.utils.ControllerUtils;
import com.example.sweater.domain.Message;
import com.example.sweater.domain.Role;
import com.example.sweater.domain.User;
import com.example.sweater.service.MainService;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

@Controller
public class MainController {

    private MainService mainService;

    @Autowired
    public MainController(MainService mainService) {
        this.mainService = mainService;
    }

    @GetMapping("/")
    public String greeting(Map<String, Object> model) {
        return "greeting";
    }

    @GetMapping("/main")
    public String main(@RequestParam(required = false, defaultValue = "") String filter, Model model) {

        Iterable<Message> messages = mainService.getFilteredMessages(filter);

        model.addAttribute("messages", messages);
        model.addAttribute("filter", filter);

        return "main";
    }

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


    @GetMapping("/download-file/{user}")
    public void downloadFile(
            @RequestParam Message message,
            HttpServletResponse response) throws Exception {

        response.setContentType("application/force-download");
        response.setHeader("Content-Disposition", "attachment; filename=" + message.getFilename());

        mainService.downloadFile(message, response);
    }

    @GetMapping("/delete-message/{user}")
    public String deleteMessage(
            @PathVariable Integer user,
            @RequestParam Message message) {

        mainService.deleteMessage(message);

        return "redirect:/user-messages/" + user;
    }
}