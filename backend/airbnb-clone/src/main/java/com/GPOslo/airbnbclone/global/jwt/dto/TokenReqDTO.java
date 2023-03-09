package com.GPOslo.airbnbclone.global.jwt.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class TokenReqDTO {
    private final String accessToken;
    private final String refreshToken;
}
