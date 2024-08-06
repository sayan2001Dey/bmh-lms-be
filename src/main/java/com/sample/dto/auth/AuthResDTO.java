package com.sample.dto.auth;

import com.fasterxml.jackson.databind.annotation.JsonAppend;
import lombok.AllArgsConstructor;
import lombok.Getter;

@JsonAppend
@Getter
@AllArgsConstructor
public class AuthResDTO {
    private String name;
    private String username;
    private Boolean admin;
    private String token;
}
