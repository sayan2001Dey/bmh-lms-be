package com.sample.dto.auth;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;

@Getter
@AllArgsConstructor
public class RegisterReqDTO {
    private String name;
    @NonNull
    private String email;
    @NonNull
    private String password;
}
