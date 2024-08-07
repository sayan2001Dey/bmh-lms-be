package com.sample.controller;

import com.sample.dto.user.UserResDTO;
import com.sample.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

    @Autowired
    private UserRepository userRepository;

    @PostMapping("login")
    public AuthResDTO login(@RequestBody AuthReqDTO authReq) {
        return authService.login(authReq);
    }

    @PostMapping("register")
    public ResponseEntity<UserResDTO> register(
            @RequestHeader(value = HttpHeaders.AUTHORIZATION, required = false) String token,
            @RequestBody AuthReqDTO authReq
    ) {
        Object[] authData = authService.verifyToken(token);
        String adminUsername = "LMS";
        if(authData == null || !((Boolean) authData[1])) {
            if(userRepository.countAllActive()!=0)
                return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
            else authReq.setAdmin(true);
        } else adminUsername = (String) authData[0];

        return new ResponseEntity<>(authService.register(authReq, adminUsername), HttpStatus.OK);
    }
}
