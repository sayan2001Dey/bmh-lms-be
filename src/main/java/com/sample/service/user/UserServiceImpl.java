package com.sample.service.user;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;

import com.sample.model.User;
import com.sample.repository.UserRepository;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;

    public void updateUser(String email, String name) {
        return;
    }

    public void deleteUser(String email) {
        return;
    }
}
