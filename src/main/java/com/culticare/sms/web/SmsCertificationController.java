package com.culticare.sms.web;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/sms-certification")
public class SmsCertificationController {

    private final SmsServiceImpl smsService;

    @PostMapping("/send")
    public ResponseEntity<?> sendSms(@RequestBody UserSmsRequestDto requestDto) throws Exception {
        try {
            smsService.sendSms(requestDto);
            return ResponseEntity.status(HttpStatus.OK).body("본인확인 sms 전송 성공");
        } catch (CustomException e) {
            e.printStackTrace();
            throw new IllegalArgumentException();
        }
    }

    //인증번호 확인
    @PostMapping("/confirm")
    public ResponseEntity<?> SmsVerification(@RequestBody UserSmsRequestDto requestDto) throws Exception{
        try {
            smsService.verifySms(requestDto);
            return ResponseEntity.status(HttpStatus.OK).body("sms 인증에 성공하였습니다.");
        } catch (CustomException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("인증에 실패하였습니다. 인증번호를 확인해주세요.");
        }
    }
}
