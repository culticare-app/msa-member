package com.culticare.member.service;

import com.culticare.common.exception.CustomException;
import com.culticare.member.controller.dto.request.MemberSaveRequestDto;
import com.culticare.member.controller.dto.response.MemberSaveResponseDto;
import com.culticare.member.dto.request.MemberLoginRequestDto;
import com.culticare.member.dto.response.MemberLoginResponseDto;
import com.culticare.member.entity.Member;
import com.culticare.member.repository.MemberRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@Transactional
@Rollback
public class MemberServiceIntegrationTest {

    @Autowired
    private MemberService memberService;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private RedisTemplate redisTemplate;



    @BeforeEach
    public void before() {
        System.out.println("Test Before");
    }

    @AfterEach
    public void after() {
        System.out.println("Test After");
        memberRepository.deleteAll(); // 테스트 후 데이터 정리
        redisTemplate.getConnectionFactory().getConnection().flushAll(); // 레디스 데이터 정리
    }

    @Test
    @DisplayName("회원가입 테스트")
    public void saveMemberTest() throws Exception {
        // Given
        MemberSaveRequestDto requestDto = MemberSaveRequestDto.builder()
                .loginId("testUser")
                .password("password123")
                .homeCountry("Korea")
                .name("Test User")
                .telephone("010-1234-5678")
                .build();

        // When
        MemberSaveResponseDto responseDto = memberService.saveMember(requestDto);

        // Then
        Optional<Member> savedMember = memberRepository.findById(responseDto.getId());
        assertThat(savedMember).isPresent();
        assertThat(savedMember.get().getLoginId()).isEqualTo("testUser");
        assertThat(passwordEncoder.matches("password123", savedMember.get().getPassword())).isTrue();
        assertThat(savedMember.get().getHomeCountry()).isEqualTo("Korea");
        assertThat(savedMember.get().getName()).isEqualTo("Test User");
        assertThat(savedMember.get().getTelephone()).isEqualTo("010-1234-5678");
    }

    @Test
    @DisplayName("로그인 테스트")
    public void loginTest() throws Exception {
        // Given
        MemberSaveRequestDto requestDto = MemberSaveRequestDto.builder()
                .loginId("testUser")
                .password("password123")
                .homeCountry("Korea")
                .name("Test User")
                .telephone("010-1234-5678")
                .build();
        memberService.saveMember(requestDto);

        MemberLoginRequestDto loginRequestDto = MemberLoginRequestDto.builder()
                .loginId("testUser")
                .password("password123")
                .build();

        // When
        MemberLoginResponseDto responseDto = memberService.login(loginRequestDto);

        // Then
        assertThat(responseDto.getAccessToken()).isNotNull();
        assertThat(responseDto.getRefreshToken()).isNotNull();
        assertThat(responseDto.getRoles()).isEqualTo("ROLE_USER");
    }

    @Test
    @DisplayName("로그아웃 테스트")
    public void logoutTest() throws Exception {
        // Given
        String accessToken = "sampleAccessToken"; // 실제로 JWT 생성 로직을 통해 유효한 토큰을 사용해야 함

        // When
        memberService.logout(accessToken);

        // Then
        String redisValue = (String) redisTemplate.opsForValue().get(accessToken);
        assertThat(redisValue).isEqualTo("blackList");
    }

    @Test
    @DisplayName("중복 로그인 아이디 체크 테스트")
    public void checkDuplicateMemberLoginIdTest() throws Exception {
        // Given
        MemberSaveRequestDto requestDto = MemberSaveRequestDto.builder()
                .loginId("duplicateUser")
                .password("password123")
                .homeCountry("Korea")
                .name("Test User")
                .telephone("010-1234-5678")
                .build();
        memberService.saveMember(requestDto);

        // When & Then
        assertThrows(CustomException.class, () -> {
            memberService.checkDuplicateMemberLoginId("duplicateUser");
        });
    }
}