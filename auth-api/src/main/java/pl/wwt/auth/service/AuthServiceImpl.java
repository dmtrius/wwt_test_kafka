package pl.wwt.auth.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import pl.wwt.auth.config.JwtUtil;
import pl.wwt.auth.dto.LoginRequest;
import pl.wwt.auth.dto.RegisterRequest;
import pl.wwt.auth.entity.User;
import pl.wwt.auth.entity.UserMapping;
import pl.wwt.auth.exception.AuthException;
import pl.wwt.auth.repository.UserRepository;

@Service
public class AuthServiceImpl implements AuthService {

    private final UserRepository repo;
    private final PasswordEncoder encoder;
    private final JwtUtil jwt;

    public AuthServiceImpl(UserRepository repo,
                       PasswordEncoder encoder,
                       JwtUtil jwt) {
        this.repo = repo;
        this.encoder = encoder;
        this.jwt = jwt;
    }

    public void register(RegisterRequest req) {
        User u = UserMapping.toEntity(req, encoder);
        repo.save(u);
    }

    public String login(LoginRequest req) {
        User u = repo.findByEmail(req.email())
                .orElseThrow(() -> new AuthException("Invalid credentials"));

        if (!encoder.matches(req.password(), u.getPasswordHash())) {
            throw new AuthException("Invalid credentials");
        }

        return jwt.generate(u.getEmail());
    }
}
