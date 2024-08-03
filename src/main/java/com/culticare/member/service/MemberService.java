package com.culticare.member.service;

import com.culticare.common.exception.CustomException;
import com.culticare.common.exception.ErrorCode;
import com.culticare.jwt.service.JwtService;
import com.culticare.jwt.web.JwtTokenProvider;
import com.culticare.jwt.web.dto.TokenDto;
import com.culticare.member.dto.request.MemberLoginRequestDto;
import com.culticare.member.dto.response.MemberLoginResponseDto;
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
import java.util.concurrent.TimeUnit;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final JwtService jwtService;
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

    public void checkDuplicateMemberLoginId(String loginId) {
        if (memberRepository.existsByLoginId(loginId)) {
            throw new CustomException(ErrorCode.EXIST_LOGIN_ID);
        }
    }

    @Transactional
    public MemberLoginResponseDto login(MemberLoginRequestDto dto) {

        // 로그인아이디 및 비밀번호 유효성 체크
        Member findMember = memberRepository.findByLoginId(dto.getLoginId())
                .orElseThrow(() -> new CustomException(ErrorCode.UNAUTHORIZED_MEMBER));

        if (!passwordEncoder.matches(dto.getPassword(), findMember.getPassword())) {
            throw new CustomException(ErrorCode.UNAUTHORIZED_PASSWORD);
        }

        TokenDto tokenDto = jwtTokenProvider.createToken(findMember.getLoginId(), findMember.getRoles());
        jwtService.saveRefreshToken(tokenDto);

        MemberLoginResponseDto memberLoginResponseDto = MemberLoginResponseDto.builder()
                .accessToken(tokenDto.getAccessToken())
                .refreshToken(tokenDto.getRefreshToken())
                .roles(findMember.getRoles().get(0))
                .build();

        return memberLoginResponseDto;
    }

    @Transactional
    public void logout(String accessToken) {
        Long expiration = jwtTokenProvider.getExpiration(accessToken);

        redisTemplate.opsForValue()
                .set(accessToken, "blackList", expiration, TimeUnit.MILLISECONDS);
    }

}
