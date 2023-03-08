package com.GPOslo.airbnbclone.global.exception.type;

public enum AuthorityExceptionType implements CustomExceptionType{
    NOT_FOUND_AUTHORITY("NOT_FOUND_AUTHORITY");

    private final String message;

    AuthorityExceptionType(String message) {
        this.message = message;
    }
    @Override
    public String getErrorCode() {
        return this.message;
    }
}
