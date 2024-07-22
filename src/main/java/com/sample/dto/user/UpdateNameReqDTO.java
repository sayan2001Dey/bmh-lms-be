package com.sample.dto.user;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;

@Getter
@AllArgsConstructor
public class UpdateNameReqDTO {
    @NonNull
    private String email;
    private String name;
}
