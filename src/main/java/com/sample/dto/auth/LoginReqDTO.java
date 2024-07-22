package com.sample.dto.auth;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;

@Getter
@AllArgsConstructor
public class LoginReqDTO {
    @NonNull
    private String email;
    @NonNull
    private String password;
}