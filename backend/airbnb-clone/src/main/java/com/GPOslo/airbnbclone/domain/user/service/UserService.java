package com.GPOslo.airbnbclone.domain.user.service;

import com.GPOslo.airbnbclone.domain.user.dto.UserSignupRequestDTO;

public interface UserService {
    Boolean userSignup(UserSignupRequestDTO userSignupRequestDTO);
//    public Boolean userLogin();
}
