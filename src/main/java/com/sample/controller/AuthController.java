package com.sample.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.sample.dto.auth.AuthResDTO;
import com.sample.dto.auth.AuthReqDTO;
import com.sample.service.auth.AuthService;

@RestController
@CrossOrigin
@RequestMapping("api/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("login")
    public AuthResDTO login(@RequestBody AuthReqDTO authReq) {
        return authService.login(authReq);
    }

    @PostMapping("register")
    public AuthResDTO register(@RequestBody AuthReqDTO authReq) {
        return authService.register(authReq);
    }
}
