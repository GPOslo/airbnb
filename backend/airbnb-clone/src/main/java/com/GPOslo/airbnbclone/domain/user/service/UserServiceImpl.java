package com.GPOslo.airbnbclone.domain.user.service;

import com.GPOslo.airbnbclone.domain.user.dto.UserSignupRequestDTO;
import com.GPOslo.airbnbclone.domain.user.domain.UserEntity;
import com.GPOslo.airbnbclone.domain.user.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {
    UserRepository userRepository;
    // validate
    // isUser?
    // DTO(or VO) to Entity
    // db insert
    private Boolean UserValidateCheck(String string) {
        return Boolean.TRUE;
    }

    private Boolean isUser(String userId) {
        return userRepository.existsById(userId);
    }

    private UserEntity DTOtoEntity(UserSignupRequestDTO userSignupRequestDTO) {
        return UserEntity.builder()
                .userId(userSignupRequestDTO.getUserId())
//                .userPassword(userSignupRequestDTO.getPassword())
                .userAddress(userSignupRequestDTO.getAddress())
                .userName(userSignupRequestDTO.getName())
                .userPhoneNumber(userSignupRequestDTO.getPhoneNumber())
//                .userSignupDateTime()
                .build();
    }

    @Override
    public Boolean userSignup(UserSignupRequestDTO userSignupRequestDTO) {
        if(!UserValidateCheck(userSignupRequestDTO.getUserId())) {
            return Boolean.FALSE;
        }

        if(isUser(userSignupRequestDTO.getUserId())) {
            return Boolean.FALSE;
        }

        try {
            userRepository.save(DTOtoEntity(userSignupRequestDTO));
        } catch(Exception e) {
            System.out.println(e);
            return Boolean.FALSE;
        }
        return Boolean.TRUE;
    }
}
