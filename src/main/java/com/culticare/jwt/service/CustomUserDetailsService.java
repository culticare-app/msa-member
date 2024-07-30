package com.culticare.jwt.service;

import com.culticare.common.exception.CustomException;
import com.culticare.common.exception.ErrorCode;
import com.culticare.jwt.service.dto.CustomUserDetails;
import com.culticare.member.entity.Member;
import com.culticare.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@RequiredArgsConstructor
@Service
@Slf4j
public class CustomUserDetailsService implements UserDetailsService {

    private final MemberRepository memberRepository;

    @Override
    public UserDetails loadUserByUsername(String loginId) throws UsernameNotFoundException {
        log.info("User ID = {}", loginId);
        Optional<Member> member = memberRepository.findByLoginId(loginId);
        if (member.isEmpty()) {
            member = memberRepository.findByLoginId(loginId);
        }
        if (member.isEmpty()) {
            throw new CustomException(ErrorCode.NOT_FOUND_MEMBER);
        }

        return new CustomUserDetails(member.get());
    }


}
