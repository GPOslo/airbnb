package com.GPOslo.airbnbclone.domain.user.repository;

import com.GPOslo.airbnbclone.domain.user.domain.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<UserEntity, String> {
}