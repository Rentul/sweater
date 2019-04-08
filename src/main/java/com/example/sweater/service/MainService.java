package com.example.sweater.service;

import com.example.sweater.domain.Message;
import com.example.sweater.domain.Role;
import com.example.sweater.domain.User;
import com.example.sweater.repos.MessageRepo;
import com.example.sweater.repos.UserRepo;
import com.example.sweater.service.file.fileManager;
import com.example.sweater.view.analytics.MessageAnalyticsView;
import com.example.sweater.view.analytics.UserAnalyticsTotalView;
import com.example.sweater.view.analytics.UserAnalyticsView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

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
        message.setAuthor(user);
        messageRepo.save(message);
    }

    public void deleteMessage(Message message) {
        deleteFileFromMsg(message);
        messageRepo.delete(message);
    }

    private void deleteFileFromMsg(Message message) {
        fileManager.deleteFile(message);
        message.setFilename(null);
        message.setDownloads(0);
        messageRepo.save(message);
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
            message.setDownloads(0);
            messageRepo.save(message);
        }
    }

    public void downloadFile(Message message,
                             HttpServletResponse response) throws Exception {

        fileManager.downLoadFile(message, response);
        message.setDownloads(message.getDownloads() + 1);
        messageRepo.save(message);
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

    public List<UserAnalyticsView> getAnalytics() {

        List<UserAnalyticsView> analyticsViews = new ArrayList<>();

        List<User> users = findAllUsers();

        for (User user : users) {
            int amountOfFiles = 0;
            int numberOfDownloads = 0;

            Set<Message> userMessages = user.getMessages();
            int numberOfMessages = userMessages.size();
            for (Message message : userMessages) {
                if (!StringUtils.isEmpty(message.getFilename())) {
                    amountOfFiles++;
                }
                numberOfDownloads += message.getDownloads();
            }
            analyticsViews.add(new UserAnalyticsView(
                    user.getId(),
                    user.getUsername(),
                    amountOfFiles,
                    numberOfDownloads,
                    numberOfMessages));
        }

        return analyticsViews;
    }

    public UserAnalyticsTotalView getTotalsForAnalytics(List<UserAnalyticsView> analyticsViews) {

        int userCount = analyticsViews.size();

        int messageCount = 0;

        int fileCount = 0;

        int downloadCount = 0;

        for (UserAnalyticsView userAnalytics : analyticsViews) {
            messageCount += userAnalytics.getNumberOfMessages();
            fileCount += userAnalytics.getAmountOfFiles();
            downloadCount += userAnalytics.getNumberOfDownloads();
        }

        return new UserAnalyticsTotalView(userCount, messageCount, fileCount, downloadCount);
    }

    public List<MessageAnalyticsView> getUserAnalyticsByFiles(User user) {
        List<MessageAnalyticsView> messageAnalyticsViews = new ArrayList<>();

        for (Message message : user.getMessages()) {
            if (!StringUtils.isEmpty(message.getFilename())) {
                messageAnalyticsViews.add(getMessageAnalytics(message));
            }
        }

        return messageAnalyticsViews;
    }

    private MessageAnalyticsView getMessageAnalytics(Message message) {
        return new MessageAnalyticsView(message.getFilename(), message.getDownloads());
    }
}
