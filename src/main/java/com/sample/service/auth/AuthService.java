package com.sample.service.auth;

import com.sample.dto.auth.AuthReqDTO;
import com.sample.dto.auth.AuthResDTO;

public interface AuthService {
    public AuthResDTO login(AuthReqDTO authReq);
    public AuthResDTO register(AuthReqDTO authReq);
    public String verifyToken(String token);
}
