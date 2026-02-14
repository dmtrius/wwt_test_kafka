package pl.wwt.auth.dto;

import java.util.UUID;

public record ProcessEvent(
        UUID requestId,
        UUID userId,
        String text) {}
