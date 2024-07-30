package com.culticare.sms.service;

import app.kidsInSeoul.sms.dto.UserSmsRequestDto;

public interface SmsService {
    void sendSms(UserSmsRequestDto requestDto);
    void verifySms(UserSmsRequestDto requestDto);
    boolean isVerify(UserSmsRequestDto requestDto);
}
