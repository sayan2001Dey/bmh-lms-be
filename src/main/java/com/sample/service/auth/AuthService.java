package com.sample.service.auth;

import com.sample.dto.auth.AuthReqDTO;
import com.sample.dto.auth.AuthResDTO;
import com.sample.dto.user.UserResDTO;

public interface AuthService {
    public AuthResDTO login(AuthReqDTO authReq);
    public UserResDTO register(AuthReqDTO authReq, String adminUsername);
    public Object[] verifyToken(String token);
}
