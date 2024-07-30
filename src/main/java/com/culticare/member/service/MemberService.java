package com.culticare.member.service;

import com.culticare.common.exception.CustomException;
import com.culticare.common.exception.ErrorCode;
import com.culticare.member.controller.dto.request.MemberSaveRequestDto;
import com.culticare.member.controller.dto.response.MemberSaveResponseDto;
import com.culticare.member.entity.Member;
import com.culticare.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final RedisTemplate redisTemplate;

    @Transactional
    public MemberSaveResponseDto saveMember(MemberSaveRequestDto dto) {

        checkDuplicateMemberLoginId(dto.getLoginId());

        Member member = Member.builder()
                .loginId(dto.getLoginId())
                .password(passwordEncoder.encode(dto.getPassword()))
                .homeCountry(dto.getHomeCountry())
                .name(dto.getName())
                .telephone(dto.getTelephone())
                .roles(Collections.singletonList("ROLE_USER"))
                .build();

        memberRepository.save(member);

        MemberSaveResponseDto memberSaveResponseDto = MemberSaveResponseDto.builder()
                .id(member.getId())
                .loginId(member.getLoginId())
                .name(member.getName())
                .telephone(member.getTelephone())
                .homeCountry(member.getHomeCountry())
                .build();

        return memberSaveResponseDto;
    }

    private void checkDuplicateMemberLoginId(String loginId) {
        if (memberRepository.existsByUserId(loginId)) {
            throw new CustomException(ErrorCode.EXIST_LOGIN_ID);
        }
    }

    //회원가입
}
