package com.culticare.member.controller.dto.request;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MemberSaveRequestDto {

    private String loginId;
    private String password;
    private String homeCountry;
    private String name;
    private String telephone;
}
