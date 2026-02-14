package pl.wwt.auth.entity;

import org.springframework.security.crypto.password.PasswordEncoder;
import pl.wwt.auth.dto.RegisterRequest;

import java.util.Objects;

public class UserMapping {
    public static User toEntity(RegisterRequest userRequest, PasswordEncoder encoder) {
        User user = new User();
        user.setLogin(!Objects.isNull(userRequest.login()) ? userRequest.login()
                : userRequest.email());
        user.setEmail(userRequest.email());
        user.setPasswordHash(encoder.encode(userRequest.password()));
        return user;
    }
}
