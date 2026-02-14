package pl.wwt.auth.dto;

public record UserDTO(
        String id,
        String login,
        String email,
        String password) {
}
