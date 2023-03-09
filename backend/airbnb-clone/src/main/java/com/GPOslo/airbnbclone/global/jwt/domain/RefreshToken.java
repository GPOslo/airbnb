package com.GPOslo.airbnbclone.global.jwt.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Getter
@NoArgsConstructor
@Table(name = "refresh_token")
@Entity
public class RefreshToken {
    @Id
    private String refreshTokenKey;

    @Column(nullable = false)
    private String refreshTokenValue;

    public void updateValue(String token) {
        this.refreshTokenValue = token;
    }

    @Builder
    public RefreshToken(String key, String value) {
        this.refreshTokenKey = key;
        this.refreshTokenValue = value;
    }
}
