package com.culticare.jwt.service;

import com.culticare.common.exception.CustomException;
import com.culticare.common.exception.ErrorCode;
import com.culticare.jwt.repository.RefreshTokenRepository;
import com.culticare.jwt.web.JwtTokenProvider;
import com.culticare.jwt.web.dto.TokenDto;
import com.culticare.jwt.web.repository.RefreshToken;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Ref;

@Service
@RequiredArgsConstructor
@Slf4j
public class JwtService {

    private final JwtTokenProvider jwtTokenProvider;
    private final RefreshTokenRepository refreshTokenRepository;

    @Transactional
    public void saveRefreshToken(TokenDto tokenDto) {

        RefreshToken refreshToken = RefreshToken.builder()
                .keyLoginId(tokenDto.getKey())
                .refreshToken(tokenDto.getRefreshToken())
                .build();
        String loginId = refreshToken.getKeyLoginId();

        if (refreshTokenRepository.existsByKeyLoginId(loginId)) {
            refreshTokenRepository.deleteByKeyLoginId(loginId);
        }
        refreshTokenRepository.save(refreshToken);
    }

    public RefreshToken getRefreshToken(String refreshToken){

        return refreshTokenRepository.findByRefreshToken(refreshToken)
                .orElseThrow(() -> new CustomException(ErrorCode.TOKEN_INVALID));
    }

    public String validateRefreshToken(String refreshToken){

        //DB에서 Refresh Token 조회
        RefreshToken getRefreshToken = getRefreshToken(refreshToken);

        String createdAccessToken = jwtTokenProvider.validateRefreshToken(getRefreshToken);

        if (createdAccessToken == null) {
            throw new CustomException(ErrorCode.TOKEN_EXPIRED);
        }

        return createdAccessToken;
    }
}
