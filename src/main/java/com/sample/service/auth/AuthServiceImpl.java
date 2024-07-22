package com.sample.service.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sample.model.User;
import com.sample.repository.UserRepository;

@Service
public class AuthServiceImpl implements AuthService {

	 @Autowired
	    private UserRepository userRepository; 
	
	 public String login(String email, String password) {
	        // Find user by email
	        User userData = userRepository.findByEmail(email);

	        // Check if user exists and if password matches
	        if (userData != null && userData.getPassword().equals(password)) {
	            return userData.getName();  // Return user's name upon successful login
	        }

	        // Return null or handle unsuccessful login (throw exception, return error message, etc.)
	        return null;
	    }

    public void register(String name, String email, String password) {
        User newUser = new User();
        newUser.setName(name);
        newUser.setEmail(email);
        newUser.setPassword(password);
        userRepository.save(newUser);
    }
}
