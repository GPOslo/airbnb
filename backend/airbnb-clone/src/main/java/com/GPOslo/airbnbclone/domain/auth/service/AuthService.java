package com.GPOslo.airbnbclone.domain.auth.service;

import com.GPOslo.airbnbclone.domain.auth.entity.AuthorityRepository;
import com.GPOslo.airbnbclone.domain.member.dto.LoginReqDTO;
import com.GPOslo.airbnbclone.domain.member.dto.MemberReqDTO;
import com.GPOslo.airbnbclone.domain.member.dto.MemberRespDTO;
import com.GPOslo.airbnbclone.domain.member.entity.Member;
import com.GPOslo.airbnbclone.domain.member.entity.MemberRepository;
import com.GPOslo.airbnbclone.domain.member.service.CustomUserDetailService;
import com.GPOslo.airbnbclone.domain.auth.entity.Authority;
import com.GPOslo.airbnbclone.domain.auth.entity.MemberAuth;
import com.GPOslo.airbnbclone.global.exception.handler.BizException;
import com.GPOslo.airbnbclone.global.exception.type.AuthorityExceptionType;
import com.GPOslo.airbnbclone.global.exception.type.JwtExceptionType;
import com.GPOslo.airbnbclone.global.exception.type.MemberExceptionType;
import com.GPOslo.airbnbclone.global.jwt.CustomEmailPasswordAuthToken;
import com.GPOslo.airbnbclone.global.jwt.TokenProvider;
import com.GPOslo.airbnbclone.global.jwt.domain.RefreshToken;
import com.GPOslo.airbnbclone.global.jwt.domain.RefreshTokenRepository;
import com.GPOslo.airbnbclone.global.jwt.domain.TokenStatus;
import com.GPOslo.airbnbclone.global.jwt.dto.TokenDTO;
import com.GPOslo.airbnbclone.global.jwt.dto.TokenReqDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Set;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {
    private final AuthenticationManager authenticationManager;
    private final MemberRepository memberRepository;
    private final AuthorityRepository authorityRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenProvider tokenProvider;
    private final RefreshTokenRepository refreshTokenRepository;
    private final CustomUserDetailService customUserDetailService;

    @Transactional
    public MemberRespDTO signup(MemberReqDTO memberRequestDto) {
        if (memberRepository.existsByEmail(memberRequestDto.getEmail())) {
            throw new BizException(MemberExceptionType.DUPLICATE_USER);
        }

        Authority authority = authorityRepository
                .findByAuthorityName(MemberAuth.ROLE_USER)
                .orElseThrow(() -> new BizException(AuthorityExceptionType.NOT_FOUND_AUTHORITY));

        Set<Authority> set = new HashSet<>();
        set.add(authority);

        Member member  = memberRequestDto.toMember(passwordEncoder, set);
        log.debug("member = {}", member);
        return MemberRespDTO.of(memberRepository.save(member));
    }

    public TokenDTO login(LoginReqDTO loginReqDTO) {
        CustomEmailPasswordAuthToken customEmailPasswordAuthToken
                = new CustomEmailPasswordAuthToken(
                        loginReqDTO.getEmail(), loginReqDTO.getPassword()
        );

        Authentication authenticate = authenticationManager.authenticate(customEmailPasswordAuthToken);
        
        String email = authenticate.getName();
        Member member = customUserDetailService.getMember(email);
        String accessToken = tokenProvider.createAccessToken(email, member.getAuthorities());
        String refreshToken = tokenProvider.createRefreshToken(email, member.getAuthorities());

        refreshTokenRepository.save(
                RefreshToken.builder()
                        .key(email)
                        .value(refreshToken)
                        .build()
        );

        return tokenProvider.createTokenDTO(accessToken, refreshToken);
    }

    @Transactional
    public TokenDTO reissue(TokenReqDTO tokenRequestDto) {
        String originAccessToken = tokenRequestDto.getAccessToken();
        String originRefreshToken = tokenRequestDto.getRefreshToken();

        TokenStatus refreshTokenFlag = tokenProvider.validateToken(originRefreshToken);

        log.debug("refreshTokenFlag = {}", refreshTokenFlag);

        if(refreshTokenFlag == TokenStatus.WRONG_TOKEN) {
            throw new BizException(JwtExceptionType.BAD_TOKEN);
        } else if(refreshTokenFlag == TokenStatus.EXPIRED_TOKEN) {
            throw new BizException(JwtExceptionType.REFRESH_TOKEN_EXPIRED);
        }

        Authentication authentication = tokenProvider.getAuthentication(originAccessToken);

        log.debug("Authentication = {}", authentication);

        RefreshToken refreshToken = refreshTokenRepository
                .findByRefreshTokenKey(authentication.getName())
                .orElseThrow(() -> new BizException(MemberExceptionType.LOGOUT_MEMBER));

        if (!refreshToken.getRefreshTokenValue().equals(originRefreshToken)) {
            throw new BizException(JwtExceptionType.BAD_TOKEN);
        }

        // 새로운 토큰 생성
        String email = tokenProvider.getMemberEmailByToken(originAccessToken);
        Member member = customUserDetailService.getMember(email);
        String newAccessToken = tokenProvider.createAccessToken(email, member.getAuthorities());
        String newRefreshToken = tokenProvider.createRefreshToken(email, member.getAuthorities());
        TokenDTO tokenDto = tokenProvider.createTokenDTO(newAccessToken, newRefreshToken);

        log.debug("refresh Origin = {}", originRefreshToken);
        log.debug("refresh New = {}", newRefreshToken);

        refreshToken.updateValue(newRefreshToken);
        return tokenDto;
    }
}
