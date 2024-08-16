package com.bmh.lms.service.user;

import com.bmh.lms.dto.auth.AuthReqDTO;
import com.bmh.lms.dto.user.UserReqDTO;
import com.bmh.lms.dto.user.UserResDTO;
import com.bmh.lms.model.User;
import com.bmh.lms.service.auth.AuthService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;

import com.bmh.lms.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AuthService authService;

    @Override
    public List<UserResDTO> getUsers() {
        List<UserResDTO> listUserResDTO = new ArrayList<>();
        userRepository.findAllActive().forEach(user -> listUserResDTO.add(userToUserResDTO(user)));
        return listUserResDTO;
    }

    @Override
    public UserResDTO getUserByUsername(String username) {
        return userToUserResDTO(userRepository.findActiveByUsername(username).orElse(null));
    }

    @Override
    public UserResDTO saveUser(AuthReqDTO req, String byUsername) {
        return authService.register(req, byUsername);
    }

    @Override
    @Transactional
    public UserResDTO updateUser(String username, UserReqDTO userReqDTO, String byUsername) {
        User user = userRepository.findActiveByUsername(username).orElse(null);
        if(user == null) return null;
        LocalDateTime ldt = LocalDateTime.now();
        user.setModified_type("UPDATED");
        user.setUpdated_by(byUsername);
        user.setUpdated_on(ldt);

        User updatedUser = userReqDTOToUpdatedUser(userReqDTO, user, ldt, byUsername);

        userRepository.save(user);
        return userToUserResDTO(userRepository.save(updatedUser));
    }

    @Override
    public Boolean deleteUser(String username, String byUsername) {
        User user = userRepository.findActiveByUsername(username).orElse(null);
        if(user == null) return false;
        LocalDateTime ldt = LocalDateTime.now();
        user.setModified_type("DELETED");
        user.setDeleted_by(byUsername);
        user.setDeleted_on(ldt);
        userRepository.save(user);
        return true;
    }

    private UserResDTO userToUserResDTO(User user) {
        return user == null ? null :
                new UserResDTO(user.getName(), user.getUsername(), user.getAdmin());
    }

    private User userReqDTOToUpdatedUser(UserReqDTO newUser, User oldUser, LocalDateTime ldt, String byUsername) {
        User res = new User();

        if(newUser.getName() == null || newUser.getName().isBlank())
            res.setName(oldUser.getName());
        else res.setName(newUser.getName());

        if(newUser.getAdmin() == null)
            res.setAdmin(oldUser.getAdmin());
        else res.setAdmin(newUser.getAdmin());

        if(newUser.getPassword() == null || newUser.getPassword().isBlank())
            res.setPassword(oldUser.getPassword());
        else res.setPassword(authService.getPasswordHash(newUser.getPassword(), oldUser.getUsername()));

        res.setUsername(oldUser.getUsername());
        res.setInserted_by(oldUser.getInserted_by());
        res.setInserted_on(oldUser.getInserted_on());
        res.setDeleted_by(oldUser.getDeleted_by());
        res.setDeleted_on(oldUser.getDeleted_on());

        res.setModified_type("INSERTED");
        res.setUpdated_by(byUsername);
        res.setUpdated_on(ldt);

        return res;
    }
}
