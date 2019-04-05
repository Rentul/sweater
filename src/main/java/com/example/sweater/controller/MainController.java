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

    @Autowired
    private MainService mainService;

    @Value("${upload.path}")
    private String uploadPath;

    @GetMapping("/")
    public String greeting(Map<String, Object> model) {
        return "greeting";
    }

    @GetMapping("/main")
    public String main(@RequestParam(required = false, defaultValue = "") String filter, Model model) {

        Iterable<Message> messages = mainService.findAll();
        if (filter != null && !filter.isEmpty()) {
            messages = mainService.findByTag(filter);
        } else {
            messages = mainService.findAll();
        }
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

        message.setAuthor(user);

        if (bindingResult.hasErrors()) {
            Map<String, String> errorsMap = ControllerUtils.getErrors(bindingResult);
            model.mergeAttributes(errorsMap);
            model.addAttribute("message", message);
        } else {
            saveFile(message, file);

            model.addAttribute("message", null);

            mainService.save(message);
        }

        Iterable<Message> messages = mainService.findAll();

        model.addAttribute("messages", messages);

        return "main";
    }

    private void saveFile(@Valid Message message,
                          @RequestParam("file") MultipartFile file) throws IOException {

        if (file != null && !file.getOriginalFilename().isEmpty()) {
            File uploadDir = new File(uploadPath);

            if (!uploadDir.exists()) {
                uploadDir.mkdir();
            }

            String uuidFile = UUID.randomUUID().toString();
            String resultFilename = uuidFile + "." + file.getOriginalFilename();

            file.transferTo(new File(uploadPath + "/" + resultFilename));

            message.setFilename(resultFilename);
        }
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

        if (message.getAuthor().equals(currentUser) || currentUser.getRoles().contains(Role.ADMIN)) {
            if (!StringUtils.isEmpty(text)) {
                message.setText(text);
            }
            if (!StringUtils.isEmpty(tag)) {
                message.setTag(tag);
            }
            File fileToDelete = new File(uploadPath + "/" + message.getFilename());
            if (!StringUtils.isEmpty(message.getFilename())) {
                fileToDelete.delete();
                message.setDownloads(0);
            }
            saveFile(message, file);
            mainService.save(message);
        }

        return "redirect:/user-messages/" + user;
    }

    @GetMapping("/download-file/{user}")
    public void downloadFile(
            @AuthenticationPrincipal User currentUser,
            @PathVariable Integer user,
            @RequestParam Message message,
            HttpServletResponse response) throws Exception {

        try {
            File fileToDownload = new File(uploadPath + "/" + message.getFilename());
            InputStream inputStream = new FileInputStream(fileToDownload);
            response.setContentType("application/force-download");
            response.setHeader("Content-Disposition", "attachment; filename=" + message.getFilename());
            IOUtils.copy(inputStream, response.getOutputStream());
            response.flushBuffer();
            inputStream.close();
            message.setDownloads(message.getDownloads() + 1);
        } catch (Exception e){
            throw new RuntimeException(e);
        }
    }

    @GetMapping("/delete-message/{user}")
    public String deleteMessage(
            @AuthenticationPrincipal User currentUser,
            @PathVariable Integer user,
            @RequestParam Message message,
            HttpServletResponse response) {

        File fileToDelete = new File(uploadPath + "/" + message.getFilename());
        if (!StringUtils.isEmpty(message.getFilename())) {
            fileToDelete.delete();
        }

        mainService.delete(message);

        return "redirect:/user-messages/" + user;
    }
}