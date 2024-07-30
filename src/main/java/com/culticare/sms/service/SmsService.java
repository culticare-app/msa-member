package com.culticare.sms.service;

import com.culticare.sms.dto.UserSmsRequestDto;

public interface SmsService {
    void sendSms(UserSmsRequestDto requestDto);
    void verifySms(UserSmsRequestDto requestDto);
    boolean isVerify(UserSmsRequestDto requestDto);
}
