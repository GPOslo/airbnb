package com.GPOslo.airbnbclone.domain.user.domain;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.sql.Date;


@Table(name = "USER", uniqueConstraints = {@UniqueConstraint(
        name = "USERID_UNIQUE",
        columnNames = {"USERID"}
)})
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class UserEntity {
    @Id
    @Column(name = "USERID", nullable = false)
    private String userId;

    @Column(name = "userPassword", nullable = false)
    private String userPassword;

    @Column(name = "userName", nullable = false)
    private String userName;

    @Column(name = "userAddress", nullable = false)
    private String userAddress;

    @Column(name = "userPhoneNumber", nullable = false)
    private String userPhoneNumber;

    @Column(name = "userSignupDateTime", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date userSignupDateTime;

    public UserEntity update(String userName, String userAddress, String userPhoneNumber) {
        this.userName = userName;
        this.userAddress = userAddress;
        this.userPhoneNumber = userPhoneNumber;
        return this;
    }

    @Builder
    public UserEntity(String userId, String userPassword, String userName, String userAddress, String userPhoneNumber, Date userSignupDateTime) {
        this.userId = userId;
        this.userPassword = userPassword;
        this.userName = userName;
        this.userAddress = userAddress;
        this.userPhoneNumber = userPhoneNumber;
        this.userSignupDateTime = userSignupDateTime;
    }

}
