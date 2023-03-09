package com.GPOslo.airbnbclone.global.exception.type;

public enum JwtExceptionType implements CustomExceptionType {
    BAD_TOKEN("BAD_TOKEN"),
    REFRESH_TOKEN_EXPIRED("REFRESH_TOKEN_EXPIRED");

    private final String message;

    JwtExceptionType(String message) {
        this.message = message;
    }
    @Override
    public String getErrorCode() {
        return this.message;
    }
}
