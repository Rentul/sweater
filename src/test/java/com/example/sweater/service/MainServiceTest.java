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
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;

/**
 * Юнит-тест основного сервиса
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class MainServiceTest {

    @Autowired
    private MainService mainService;

    @MockBean
    private MessageRepo messageRepo;

    @MockBean
    private UserRepo userRepo;

    @MockBean
    private FileManager fileManager;

    /**
     * Тест на сохранение сообщения в базе
     */
    @Test
    public void saveMessageTest() {

        User user = new User();

        Message message = new Message();

        MultipartFile file = new MockMultipartFile("filename", new byte[0]);

        mainService.saveMessage(user, message, file);

        Assert.assertEquals(message.getAuthor(), user);
        Mockito.verify(fileManager, Mockito.times(1)).saveFile(message, file);
        Mockito.verify(messageRepo, Mockito.times(1)).save(message);
    }

    /**
     * Тест на удаление сообщения из базы
     */
    @Test
    public void deleteMessageTest() {

        User user = new User();
        Message message = new Message();

        user.setId(1);
        user.setUsername("user");
        message.setAuthor(user);
        message.setId(1);

        mainService.deleteMessage(message);

        Assert.assertNull(message.getFilename());
        Mockito.verify(fileManager, Mockito.times(1)).deleteFile(message);
        Mockito.verify(messageRepo, Mockito.times(1)).delete(message);
    }

    /**
     * Тест на обновление сообщения в базе
     */
    @Test
    public void updateMessageTest() {

        User user = new User();
        Message message = new Message();
        String text = "text";
        String tag = "tag";
        MultipartFile file = new MockMultipartFile("filename", new byte[0]);

        user.setId(1);
        user.setUsername("user");
        message.setAuthor(user);
        message.setId(1);

        mainService.updateMessage(user, message, text, tag, file);

        Assert.assertEquals(message.getDownloads(), 0);
        Mockito.verify(fileManager, Mockito.times(1)).saveFile(message, file);
        Mockito.verify(messageRepo, Mockito.times(1)).save(message);
    }

    /**
     * Тест на обновление сообщения в базе админом
     */
    @Test
    public void updateMessageByAdminTest() {

        User admin = new User();
        User author = new User();
        Message message = new Message();
        String text = "text";
        String tag = "tag";
        MultipartFile file = new MockMultipartFile("filename", new byte[0]);
        Set<Role> roles = new HashSet<>();
        roles.add(Role.ADMIN);

        author.setId(1);
        admin.setId(2);
        admin.setRoles(roles);
        message.setAuthor(author);
        message.setId(1);

        mainService.updateMessage(admin, message, text, tag, file);

        Assert.assertEquals(message.getDownloads(), 0);
        Mockito.verify(fileManager, Mockito.times(1)).saveFile(message, file);
        Mockito.verify(messageRepo, Mockito.times(1)).save(message);
    }

    /**
     * Тест на загрузку файла
     */
    @Test
    public void downloadFileTest() {

        Message message = new Message();
        MultipartFile file = new MockMultipartFile("filename", new byte[0]);
        MockHttpServletResponse response = new MockHttpServletResponse();

        message.setId(1);
        message.setDownloads(1);


        mainService.downloadFile(message, response);

        Assert.assertEquals(message.getDownloads(), 2);
        Mockito.verify(fileManager, Mockito.times(1)).downLoadFile(message, response);
        Mockito.verify(messageRepo, Mockito.times(1)).save(message);
    }

    /**
     * Тест на получение отфильтрованных сообщений
     */
    @Test
    public void getFilteredMessagesTest() {

        String filter = "filter";

        List<Message> messages = new ArrayList<>();
        Mockito.doReturn(messages)
                .when(messageRepo)
                .findByTag(filter);

        Assert.assertEquals(mainService.getFilteredMessages(filter), messages);

        Mockito.verify(messageRepo, Mockito.times(1)).findByTag(filter);
    }

    /**
     * Тест на получение списка всех пользователей
     */
    @Test
    public void findAllUsersTest() {

        List<User> users = new ArrayList<>();
        Mockito.doReturn(users)
                .when(userRepo)
                .findAll();

        Assert.assertEquals(mainService.findAllUsers(), users);

        Mockito.verify(userRepo, Mockito.times(1)).findAll();
    }

    /**
     * Тест на получение аналитики по всем пользователям
     */
    @Test
    public void getAnalyticsTest() {

        List<UserAnalyticsView> userAnalyticsViews = new ArrayList<>();
        Mockito.doReturn(userAnalyticsViews)
                .when(userRepo)
                .findAll();

        Assert.assertEquals(mainService.findAllUsers(), userAnalyticsViews);

        Mockito.verify(userRepo, Mockito.times(1)).findAll();
    }

    /**
     * Тест на получение общих итогов по аналитике по всем пользователям
     */
    @Test
    public void getTotalsForAnalyticsTest() {

        List<UserAnalyticsView> userAnalyticsViews = new ArrayList<>();

        userAnalyticsViews.add(new UserAnalyticsView(
                1,
                "user1",
                5,
                6,
                5));

        userAnalyticsViews.add(new UserAnalyticsView(
                2, "user2",
                3,
                2,
                4));

        UserAnalyticsTotalView totalsForAnalytics = mainService.getTotalsForAnalytics(userAnalyticsViews);

        Assert.assertEquals(totalsForAnalytics.getMessageCount(), 9);
        Assert.assertEquals(totalsForAnalytics.getDownloadCount(), 8);
        Assert.assertEquals(totalsForAnalytics.getFileCount(), 8);
        Assert.assertEquals(totalsForAnalytics.getUserCount(), 2);
    }

    /**
     * Тест на получение детальной аналитики для одного пользователя по его файлам
     */
    @Test
    public void getUserAnalyticsByFilesTest() {

        User user = new User();

        Set<Message> messages = new HashSet<>();

        Message msg1 = new Message();
        msg1.setFilename("filename1");
        msg1.setDownloads(3);

        Message msg2 = new Message();
        msg2.setFilename("filename2");
        msg2.setDownloads(5);

        Message msg3 = new Message();

        messages.add(msg1);
        messages.add(msg2);
        messages.add(msg3);

        user.setMessages(messages);

        List<MessageAnalyticsView> userAnalyticsByFiles = mainService.getUserAnalyticsByFiles(user);

        userAnalyticsByFiles.sort(Comparator.comparingInt(MessageAnalyticsView::getDownloads));

        Assert.assertEquals(userAnalyticsByFiles.size(), 2);
        Assert.assertEquals(userAnalyticsByFiles.get(0).getDownloads(), msg1.getDownloads());
        Assert.assertEquals(userAnalyticsByFiles.get(0).getFilename(), msg1.getFilename());
        Assert.assertEquals(userAnalyticsByFiles.get(1).getDownloads(), msg2.getDownloads());
        Assert.assertEquals(userAnalyticsByFiles.get(1).getFilename(), msg2.getFilename());
    }
}
