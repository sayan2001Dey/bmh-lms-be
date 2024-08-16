package com.bmh.lms.dto.user;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserReqDTO {
    private String name;
    private String password;
    private Boolean admin;
}
