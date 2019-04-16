package com.example.sweater.service.captcha;

import com.example.sweater.domain.dto.CaptchaResponseDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;

/**
 * Сервис валидации капчи
 */
@Service
public class CaptchaValidator {

    private static final String CAPTCHA_URL = "https://www.google.com/recaptcha/api/siteverify?secret=%s&response=%s";

    @Autowired
    private RestTemplate restTemplate;

    @Value("${recaptcha.secret}")
    private String secret;

    /**
     * Проверить валидность капчи
     *
     * @param clientCaptchaResponse результаты прохождения капчи на клиенте
     * @return результаты прохождения капчи от google
     */
    public CaptchaResponseDto validate(String clientCaptchaResponse) {
        final String url = String.format(CAPTCHA_URL, secret, clientCaptchaResponse);
        return restTemplate.postForObject(url, Collections.EMPTY_LIST, CaptchaResponseDto.class);
    }
}
