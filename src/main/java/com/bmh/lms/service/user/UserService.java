package com.bmh.lms.service.user;

public interface UserService {
    public void updateUser(String email, String name);
    public void deleteUser(String email);
}
