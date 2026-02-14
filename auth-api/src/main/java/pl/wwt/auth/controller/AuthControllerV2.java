package pl.wwt.auth.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.wwt.auth.dto.TokenResponse;
import pl.wwt.auth.dto.UserDTO;
import pl.wwt.auth.exception.AuthException;
import pl.wwt.auth.service.UserService;

@RestController
@RequestMapping("/api/v2/auth")
public class AuthControllerV2 {

    private final UserService service;

    public AuthControllerV2(UserService service) {
        this.service = service;
    }

    @PostMapping("/register")
    public ResponseEntity<UserDTO> register(@RequestBody UserDTO user) {
        service.registerUser(user);
        return ResponseEntity.status(201).build();
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody UserDTO user) {
        String token;
        try {
            token = service.login(user);
        } catch (AuthException _) {
            return ResponseEntity.status(401).build();
        }
        return ResponseEntity.ok(new TokenResponse(token));
    }
}
