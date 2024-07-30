package com.culticare.member.dto.response;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MemberLoginResponseDto {

    private String accessToken;
    private String refreshToken;
    private String roles;
}
