package com.GPOslo.airbnbclone.domain.member.dto;

import com.GPOslo.airbnbclone.domain.member.entity.Member;
import com.GPOslo.airbnbclone.domain.auth.entity.Authority;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Set;

@Getter
@Setter
public class MemberReqDTO {
    private String username;
    private String email;
    private String password;

    public Member toMember(PasswordEncoder passwordEncoder,
                           Set<Authority> set) {
        return Member.builder()
                .username(this.username)
                .email(this.email)
                .authorities(set)
                .password(passwordEncoder.encode(this.password))
                .build();
    }

}
