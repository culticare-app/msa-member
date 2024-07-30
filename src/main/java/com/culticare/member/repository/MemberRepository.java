package com.culticare.member.repository;

import com.culticare.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {

    Optional<Member> findByLoginId(String loginId);
    Optional<Member> findById(Long id);

    boolean existsByNickname(String nickname);
    boolean existsByUserId(String userId);
}
