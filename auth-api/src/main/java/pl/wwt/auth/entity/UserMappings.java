package pl.wwt.auth.entity;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import pl.wwt.auth.dto.UserDTO;

import java.util.Objects;
import java.util.UUID;

public class UserMappings {
    private final static PasswordEncoder encoder = new BCryptPasswordEncoder();
    public static User toEntity(UserDTO userDTO) {
        User user = new User();
        user.setId((!Objects.isNull(userDTO.id())) ? UUID.fromString(userDTO.id()) : null);
        user.setLogin(userDTO.login());
        user.setEmail(userDTO.email());
        user.setPasswordHash(encoder.encode(userDTO.password()));
        return user;
    }
}
