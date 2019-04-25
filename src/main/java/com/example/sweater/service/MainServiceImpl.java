package com.example.sweater.service;

import com.example.sweater.domain.Message;
import com.example.sweater.domain.Role;
import com.example.sweater.domain.User;
import com.example.sweater.repos.MessageRepo;
import com.example.sweater.repos.UserRepo;
import com.example.sweater.service.file.FileManager;
import com.example.sweater.view.analytics.MessageAnalyticsView;
import com.example.sweater.view.analytics.UserAnalyticsTotalView;
import com.example.sweater.view.analytics.UserAnalyticsView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * {@inheritDoc}
 */
@Service
public class MainServiceImpl implements MainService{

    private final MessageRepo messageRepo;

    private final UserRepo userRepo;

    private final FileManager fileManager;

    /**
     * Конструктор
     * @param messageRepo репозиторий сообщений
     * @param userRepo репозиторий пользователей
     * @param fileManager менеджер файлов
     */
    @Autowired
    public MainServiceImpl(final MessageRepo messageRepo,
                           final UserRepo userRepo,
                           final FileManager fileManager) {
        this.messageRepo = messageRepo;
        this.userRepo = userRepo;
        this.fileManager = fileManager;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public void saveMessage(final User user, final Message message, final MultipartFile file) {
        fileManager.saveFile(message, file);
        message.setAuthor(user);
        messageRepo.save(message);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public void deleteMessage(final Message message) {
        deleteFileFromMsg(message);
        messageRepo.delete(message);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public void updateMessage(final User currentUser,
                              final Message message,
                              final String text,
                              final String tag,
                              final MultipartFile file) {

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

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public void downloadFile(final Message message,
                             final HttpServletResponse response) {

        fileManager.downLoadFile(message, response);
        message.setDownloads(message.getDownloads() + 1);
        messageRepo.save(message);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public Iterable<Message> getFilteredMessages(final String filter) {
        final Iterable<Message> messages;
        if (!StringUtils.isEmpty(filter)) {
            messages = findByTag(filter);
        } else {
            messages = findAll();
        }
        return messages;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public List<User> findAllUsers() {
        return userRepo.findAll();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public List<UserAnalyticsView> getAnalytics() {

        final List<UserAnalyticsView> analyticsViews = new ArrayList<>();

        final List<User> users = findAllUsers();

        for (final User user : users) {

            final Set<Message> userMessages = user.getMessages();

            int amountOfFiles = 0,
                    numberOfDownloads = 0,
                    numberOfMessages = userMessages.size();

            for (final Message message : userMessages) {
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

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public UserAnalyticsTotalView getTotalsForAnalytics(final List<UserAnalyticsView> analyticsViews) {

        int userCount = analyticsViews.size(),
                messageCount = 0,
                fileCount = 0,
                downloadCount = 0;

        for (final UserAnalyticsView userAnalytics : analyticsViews) {
            messageCount += userAnalytics.getNumberOfMessages();
            fileCount += userAnalytics.getAmountOfFiles();
            downloadCount += userAnalytics.getNumberOfDownloads();
        }

        return new UserAnalyticsTotalView(userCount, messageCount, fileCount, downloadCount);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public List<MessageAnalyticsView> getUserAnalyticsByFiles(final User user) {
        final List<MessageAnalyticsView> messageAnalyticsViews = new ArrayList<>();

        for (final Message message : user.getMessages()) {
            if (!StringUtils.isEmpty(message.getFilename())) {
                messageAnalyticsViews.add(getMessageAnalytics(message));
            }
        }

        return messageAnalyticsViews;
    }

    private void deleteFileFromMsg(final Message message) {
        fileManager.deleteFile(message);
        message.setFilename(null);
        message.setDownloads(0);
        messageRepo.save(message);
    }

    private MessageAnalyticsView getMessageAnalytics(final Message message) {
        return new MessageAnalyticsView(message.getFilename(), message.getDownloads());
    }

    private Iterable<Message> findAll() {
        return messageRepo.findAll();
    }

    private Iterable<Message> findByTag(final String filter) {
        return messageRepo.findByTag(filter);
    }
}
