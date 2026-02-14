package pl.wwt.auth.service;

import pl.wwt.auth.dto.LoginRequest;
import pl.wwt.auth.dto.RegisterRequest;

public interface AuthService {
    void register(RegisterRequest req);
    String login(LoginRequest req);
}
