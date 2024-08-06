package com.sample.dto.auth;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class AuthReqDTO {
    private String name;
    private Boolean admin;
    @NonNull
    private String username;
    @NonNull
    private String password;
}
