package com.bmh.lms.dto.user;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserResDTO {
    private String name;
    private String username;
    private Boolean admin;
}
