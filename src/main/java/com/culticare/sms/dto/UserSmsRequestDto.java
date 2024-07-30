package com.culticare.sms.dto;

import lombok.Getter;

@Getter
public class UserSmsRequestDto {
    private String phone;
    private String certificationNumber;
}
