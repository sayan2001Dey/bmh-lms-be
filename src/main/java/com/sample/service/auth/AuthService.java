package com.sample.service.auth;

public interface AuthService {
    public String login(String email, String password);
    public void register(String name, String email, String password);
}
