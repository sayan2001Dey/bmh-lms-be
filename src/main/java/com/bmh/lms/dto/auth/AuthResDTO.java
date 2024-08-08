package com.bmh.lms.dto.auth;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AuthResDTO {
    private String name;
    private String username;
    private Boolean admin;
    private String token;
}
