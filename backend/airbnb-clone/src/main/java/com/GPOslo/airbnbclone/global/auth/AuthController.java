package com.GPOslo.airbnbclone.global.auth;

import com.GPOslo.airbnbclone.domain.member.dto.LoginReqDTO;
import com.GPOslo.airbnbclone.domain.member.dto.MemberReqDTO;
import com.GPOslo.airbnbclone.domain.member.dto.MemberRespDTO;
import com.GPOslo.airbnbclone.global.auth.service.AuthService;
import com.GPOslo.airbnbclone.global.jwt.dto.TokenDTO;
import com.GPOslo.airbnbclone.global.jwt.dto.TokenReqDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/signup")
    public MemberRespDTO signup(@RequestBody MemberReqDTO memberRequestDto) {
        log.debug("memberRequestDto = {}", memberRequestDto);
        return authService.signup(memberRequestDto);
    }

    @PostMapping("/login")
    public TokenDTO login(@RequestBody LoginReqDTO loginReqDTO) {
        return authService.login(loginReqDTO);
    }

    @PostMapping("/reissue")
    public TokenDTO reissue(@RequestBody TokenReqDTO tokenReqDTO) {
        return authService.reissue(tokenReqDTO);
    }
}
