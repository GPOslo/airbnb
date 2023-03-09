package com.GPOslo.airbnbclone.global.exception.handler;


import com.GPOslo.airbnbclone.global.exception.type.CustomExceptionType;

public class BizException extends CustomException {
    public BizException(CustomExceptionType error) {
        super(error);
    }
}
