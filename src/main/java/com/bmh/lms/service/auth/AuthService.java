package com.bmh.lms.service.auth;

import com.bmh.lms.dto.auth.AuthReqDTO;
import com.bmh.lms.dto.auth.AuthResDTO;
import com.bmh.lms.dto.user.UserResDTO;

public interface AuthService {
    AuthResDTO login(AuthReqDTO authReq);
    UserResDTO register(AuthReqDTO authReq, String adminUsername);
    Object[] verifyToken(String token);
    String getPasswordHash(String password, String username);
}
