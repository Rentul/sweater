package com.example.sweater.service;

import com.example.sweater.domain.User;
import com.example.sweater.domain.dto.CaptchaResponseDto;

public interface RegistrationService {

    CaptchaResponseDto getCaptchaResponse(String clientCaptchaResponse);

    boolean addUser(User user);

    boolean activateUser(String code);
}
