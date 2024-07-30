package com.culticare.jwt.repository;

import com.culticare.jwt.web.repository.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {

    Optional<RefreshToken> findByRefreshToken(String refreshToken);

    boolean existsByKeyLoginId(String loginId);

    void deleteByKeyLoginId(String loginId);
}
