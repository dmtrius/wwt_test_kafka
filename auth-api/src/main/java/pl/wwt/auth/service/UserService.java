package pl.wwt.auth.service;

import pl.wwt.auth.dto.UserDTO;
import pl.wwt.auth.entity.User;

public interface UserService {
    String login(UserDTO user);
    void registerUser(UserDTO user);
    User findByEmail(String email);
}
