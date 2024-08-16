package com.bmh.lms.service.user;

import com.bmh.lms.dto.auth.AuthReqDTO;
import com.bmh.lms.dto.user.UserReqDTO;
import com.bmh.lms.dto.user.UserResDTO;

import java.util.List;

public interface UserService {
    List<UserResDTO> getUsers();
    UserResDTO getUserByUsername(String username);
    UserResDTO saveUser(AuthReqDTO req, String byUsername);
    UserResDTO updateUser(String username, UserReqDTO userReqDTO, String byUsername);
    Boolean deleteUser(String username, String byUsername);
}
