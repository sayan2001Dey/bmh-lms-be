package com.bmh.lms.service.auth;

import com.bmh.lms.dto.auth.AuthReqDTO;
import com.bmh.lms.dto.auth.AuthResDTO;
import com.bmh.lms.dto.user.UserResDTO;

public interface AuthService {
    public AuthResDTO login(AuthReqDTO authReq);
    public UserResDTO register(AuthReqDTO authReq, String adminUsername);
    public Object[] verifyToken(String token);
}
