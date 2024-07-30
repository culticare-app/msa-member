package com.culticare.member.controller.dto.response;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MemberSaveResponseDto {

    private Long id;
    private String name;
    private String loginId;
    private String homeCountry;
    private String telephone;
}
