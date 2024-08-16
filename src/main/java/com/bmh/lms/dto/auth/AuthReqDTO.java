package com.bmh.lms.dto.auth;

import lombok.*;

@Getter
@Setter
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
