package com.GPOslo.airbnbclone.domain.member.service;

import com.GPOslo.airbnbclone.domain.member.entity.Member;
import com.GPOslo.airbnbclone.domain.member.entity.MemberRepository;
import com.GPOslo.airbnbclone.global.exception.handler.BizException;
import com.GPOslo.airbnbclone.global.auth.entity.Authority;
import com.GPOslo.airbnbclone.global.exception.type.MemberExceptionType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
@Slf4j
public class CustomUserDetailService implements UserDetailsService {
    private final MemberRepository memberRepository;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String email) throws BizException {
        log.debug("CustomUserDetailService -> email  = {}", email);
        return memberRepository.findByEmail(email)
                .map(this::createUserDetails)
                .orElseThrow(() -> new BizException(MemberExceptionType.NOT_FOUND_USER));
    }

    @Transactional(readOnly = true)
    public Member getMember(String email) throws BizException {
        return memberRepository.findByEmail(email)
                .orElseThrow(() -> new BizException(MemberExceptionType.NOT_FOUND_USER));
    }

    private UserDetails createUserDetails(Member member) {
        List<SimpleGrantedAuthority> authList = member.getAuthorities()
                .stream()
                .map(Authority::getAuthorityName)
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());

        authList.forEach(o-> log.debug("authList -> {}", o.getAuthority()));

        return new User(
                member.getEmail(),
                member.getPassword(),
                authList
        );
    }
}
