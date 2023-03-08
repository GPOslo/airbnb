package com.GPOslo.airbnbclone.global.jwt.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class TokenDTO {
    private String accessToken;
    private String refreshToken;
    private String grantType;
}