package com.GPOslo.airbnbclone.global.exception.handler;


import com.GPOslo.airbnbclone.global.exception.type.CustomExceptionType;

import java.io.Serializable;

public class CustomException extends RuntimeException implements Serializable {
    private final CustomExceptionType errorCode;
    protected CustomException(CustomExceptionType errorCode) {
        super(errorCode.getErrorCode());
        this.errorCode = errorCode;
    }

    public CustomExceptionType getBaseExceptionType() {
        return this.errorCode;
    }
}
