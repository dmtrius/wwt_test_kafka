package pl.wwt.auth.service;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.wwt.auth.config.JwtUtil;
import pl.wwt.auth.dto.UserDTO;
import pl.wwt.auth.entity.User;
import pl.wwt.auth.entity.UserMappings;
import pl.wwt.auth.exception.AuthException;
import pl.wwt.auth.repository.UserRepository;

@Service
@Transactional
public class UserServiceImpl implements UserService {

    private final PasswordEncoder encoder = new BCryptPasswordEncoder();
    private final JwtUtil jwt;
    private final UserRepository repo;

    public UserServiceImpl(JwtUtil jwt, UserRepository repo) {
        this.jwt = jwt;
        this.repo = repo;
    }

    @Override
    public String login(UserDTO user) {
        User u = findByEmail(user.email());
        if (!encoder.matches(user.password(), u.getPasswordHash())) {
            throw new AuthException("Invalid credentials");
        }
        return jwt.generate(u.getEmail());
    }

    @Override
    public void registerUser(UserDTO user) {
        repo.save(UserMappings.toEntity(user));
    }

    @Transactional(readOnly = true)
    @Override
    public User findByEmail(String email) {
        return repo.findByEmail(email).orElseThrow();
    }
}
