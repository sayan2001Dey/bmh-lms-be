package com.sample.dto.auth;

import lombok.*;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class AuthReqDTO {
    @Setter
    private String name;
    @Setter
    private Boolean admin;
    @NonNull
    private String username;
    @NonNull
    private String password;
}
