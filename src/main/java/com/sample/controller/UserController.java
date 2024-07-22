package com.sample.controller;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.web.bind.annotation.*;

import com.sample.dto.auth.RegisterResDTO;
import com.sample.dto.user.UpdateNameReqDTO;
import com.sample.service.user.UserService;

@RestController
@RequestMapping("api/user")
public class UserController {
    @Autowired
    private UserService userService;
    @CrossOrigin(origins = "http://localhost:4200")
    @PatchMapping
    public RegisterResDTO updateUser(@RequestBody UpdateNameReqDTO updateUserDTO) {
        userService.updateUser(updateUserDTO.getEmail(), updateUserDTO.getName());
        return new RegisterResDTO("Success");
    }

    @DeleteMapping
    public RegisterResDTO deleteUser(@RequestParam String email) {
        userService.deleteUser(email);
        return new RegisterResDTO("Success");
    }
}
