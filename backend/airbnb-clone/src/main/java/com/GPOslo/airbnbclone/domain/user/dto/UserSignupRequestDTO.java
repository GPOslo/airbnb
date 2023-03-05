package com.GPOslo.airbnbclone.domain.user.dto;

import lombok.Data;

@Data
public class UserSignupRequestDTO {
    private String userId;
    private String password;
    private String name;
    private String address;
    private String phoneNumber;
}
