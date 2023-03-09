package com.GPOslo.airbnbclone.domain.member.dto;

import com.GPOslo.airbnbclone.domain.member.entity.Member;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MemberRespDTO {
    private String username;
    private String email;

    public static MemberRespDTO of(Member member) {
        return MemberRespDTO.builder()
                .email(member.getEmail())
                .username(member.getUsername())
                .build();
    }
}
