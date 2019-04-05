package com.example.sweater.service.captcha;

import com.example.sweater.domain.dto.CaptchaResponseDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;

@Service
public class CaptchaValidator {

    private static final String CAPTCHA_URL = "https://www.google.com/recaptcha/api/siteverify?secret=%s&response=%s";

    @Autowired
    private RestTemplate restTemplate;

    @Value("${recaptcha.secret}")
    private String secret;

    public CaptchaResponseDto validate(String clientCaptchaResponse) {
        String url = String.format(CAPTCHA_URL, secret, clientCaptchaResponse);
        return restTemplate.postForObject(url, Collections.EMPTY_LIST, CaptchaResponseDto.class);
    }
}
