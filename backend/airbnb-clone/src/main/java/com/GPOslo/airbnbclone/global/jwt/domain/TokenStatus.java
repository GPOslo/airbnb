package com.GPOslo.airbnbclone.global.jwt.domain;

public enum TokenStatus {
    VALID_TOKEN("VALID_TOKEN"),
    EXPIRED_TOKEN("EXPIRED_TOKEN"),
    WRONG_TOKEN("WRONG_TOKEN");

    private String abbreviation;

    TokenStatus(String abbreviation) {
        this.abbreviation = abbreviation;
    }

    public String getAbbreviation() {
        return this.abbreviation;
    }
}
