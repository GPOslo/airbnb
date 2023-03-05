package com.GPOslo.airbnbclone.domain.user.controller;

import com.GPOslo.airbnbclone.domain.user.dto.UserSignupRequestDTO;
import com.GPOslo.airbnbclone.domain.user.service.UserService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("user")
public class UserController {

    UserService userService;
    private enum signupResponse {
        SUCCESS,
        FAIL
    }
    @GetMapping("signup")
    public signupResponse signup(@RequestBody UserSignupRequestDTO userSignupRequestDTO) {
        if (userService.userSignup(userSignupRequestDTO)) {
            return signupResponse.SUCCESS;
        } else {
            return signupResponse.FAIL;
        }
    }
}
