package pl.wwt.auth.dto;

public record LoginRequest(
        String email,
        String password) {}
