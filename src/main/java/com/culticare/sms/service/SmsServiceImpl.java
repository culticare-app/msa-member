package com.culticare.sms.service;

import app.kidsInSeoul.common.exception.CustomException;
import app.kidsInSeoul.common.exception.ErrorCode;
import app.kidsInSeoul.sms.dao.SmsCertificationDao;
import app.kidsInSeoul.sms.dto.UserSmsRequestDto;
import app.kidsInSeoul.sms.util.SmsCertificationUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class SmsServiceImpl implements SmsService{
    private final SmsCertificationUtil smsUtil;
    private final SmsCertificationDao smsCertificationDao;

    public void sendSms(UserSmsRequestDto requestDto) {
        String to = requestDto.getPhone();
        int randomNumber = (int) (Math.random() * 9000) + 1000;
        String certificationNumber = String.valueOf(randomNumber);
        smsUtil.sendSms(to, certificationNumber);
        smsCertificationDao.createSmsCertification(to,certificationNumber);
    }

    public void verifySms(UserSmsRequestDto requestDto) {
        if (isVerify(requestDto)) {
            throw new CustomException(ErrorCode.SMS_CERTIFICATION_NUMBER_MISMATCH_EXCEPTION);
        }
        smsCertificationDao.removeSmsCertification(requestDto.getPhone());
    }

    public boolean isVerify(UserSmsRequestDto requestDto) {
        return !(smsCertificationDao.hasKey(requestDto.getPhone()) &&
                smsCertificationDao.getSmsCertification(requestDto.getPhone())
                        .equals(requestDto.getCertificationNumber()));
    }
}
