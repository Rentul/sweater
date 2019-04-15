package com.example.sweater.service;

import com.example.sweater.domain.Message;
import com.example.sweater.domain.User;
import com.example.sweater.view.analytics.MessageAnalyticsView;
import com.example.sweater.view.analytics.UserAnalyticsTotalView;
import com.example.sweater.view.analytics.UserAnalyticsView;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

public interface MainService {

    void saveMessage(User user, Message message, MultipartFile file) throws IOException;

    void deleteMessage(Message message);

    void updateMessage(User currentUser,
                       Message message,
                       String text,
                       String tag,
                       MultipartFile file) throws IOException;

    void downloadFile(Message message, HttpServletResponse response) throws Exception;

    Iterable<Message> getFilteredMessages(String filter);

    List<User> findAllUsers();

    List<UserAnalyticsView> getAnalytics();

    UserAnalyticsTotalView getTotalsForAnalytics(List<UserAnalyticsView> analyticsViews);

    List<MessageAnalyticsView> getUserAnalyticsByFiles(User user);

}
