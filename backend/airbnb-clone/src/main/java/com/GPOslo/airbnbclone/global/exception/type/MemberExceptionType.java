package com.GPOslo.airbnbclone.global.exception.type;

public enum MemberExceptionType implements CustomExceptionType{
    NOT_FOUND_USER("NOT_FOUND_USER"),
    NOT_FOUND_PASSWORD("NOT_FOUND_PASSWORD"),
    WRONG_PASSWORD("WRONG_PASSWORD"),
    LOGOUT_MEMBER("LOGOUT_MEMBER"),
    DUPLICATE_USER("DUPLICATE_USER");


    private final String message;

    MemberExceptionType(String message) {
        this.message = message;
    }

    public String getErrorCode() {
        return this.message;
    }
}
