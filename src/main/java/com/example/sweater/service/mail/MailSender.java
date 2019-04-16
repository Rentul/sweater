package com.example.sweater.service.mail;

import com.example.sweater.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

/**
 * Сервис для отправки электронных писем
 */
@Service
public class MailSender {

    @Autowired
    private JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String username;

    @Value("${server.address.string}")
    private String serverAddress;

    /**
     * Геттер отправщика писем
     *
     * @return отправщик писем
     */
    public JavaMailSender getMailSender() {
        return mailSender;
    }

    /**
     * Сеттер отправщика писем
     *
     * @param mailSender отправщик писем
     */
    public void setMailSender(final JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    /**
     * Отправить сообщение
     *
     * @param user пользователь, которому будет отправлено сообщение
     */
    public void sendMessage(final User user) {
        if (!StringUtils.isEmpty(user.getEmail())) {
            final String message = String.format(
                    "Hello, %s! \n" +
                            "Welcome to Sweater. Please visit next link: %s/activate/%s",
                    user.getUsername(),
                    serverAddress,
                    user.getActivationCode()
            );

            send(user.getEmail(), "Activation code", message);
        }
    }

    private void send(final String emailTo, final String subject, final String message) {
        final SimpleMailMessage mailMessage = new SimpleMailMessage();

        mailMessage.setFrom(username);
        mailMessage.setTo(emailTo);
        mailMessage.setSubject(subject);
        mailMessage.setText(message);

        mailSender.send(mailMessage);
    }
}
