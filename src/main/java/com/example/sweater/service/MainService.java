package com.example.sweater.service;

import com.example.sweater.domain.Message;
import com.example.sweater.domain.Role;
import com.example.sweater.domain.User;
import com.example.sweater.repos.MessageRepo;
import com.example.sweater.repos.UserRepo;
import com.example.sweater.service.file.fileManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@Service
public class MainService {

    private MessageRepo messageRepo;

    private UserRepo userRepo;

    private fileManager fileManager;

    @Autowired
    public MainService(MessageRepo messageRepo, UserRepo userRepo, fileManager fileManager) {
        this.messageRepo = messageRepo;
        this.userRepo = userRepo;
        this.fileManager = fileManager;
    }

    private Iterable<Message> findAll() {
        return messageRepo.findAll();
    }

    private Iterable<Message> findByTag(String filter) {
        return messageRepo.findByTag(filter);
    }

    public void saveMessage(User user, Message message, MultipartFile file) throws IOException {
        fileManager.saveFile(message, file);
        messageRepo.save(message);
        message.setAuthor(user);
    }

    public void deleteMessage(Message message) {
        deleteFileFromMsg(message);
        messageRepo.delete(message);
    }

    private void deleteFileFromMsg(Message message) {
        fileManager.deleteFile(message);
    }

    public void updateMessage(User currentUser, Message message, String text, String tag, MultipartFile file) throws IOException {
        if (message.getAuthor().equals(currentUser) || currentUser.getRoles().contains(Role.ADMIN)) {
            if (!StringUtils.isEmpty(text)) {
                message.setText(text);
            }
            if (!StringUtils.isEmpty(tag)) {
                message.setTag(tag);
            }
            fileManager.deleteFile(message);
            fileManager.saveFile(message, file);
            messageRepo.save(message);
        }
    }

    public void downloadFile(Message message,
                             HttpServletResponse response) throws Exception {

        fileManager.downLoadFile(message, response);
        message.setDownloads(message.getDownloads() + 1);
    }

    public Iterable<Message> getFilteredMessages(String filter) {
        Iterable<Message> messages;
        if (!StringUtils.isEmpty(filter)) {
            messages = findByTag(filter);
        } else {
            messages = findAll();
        }
        return messages;
    }

    public List<User> findAllUsers() {
        return userRepo.findAll();
    }
}
