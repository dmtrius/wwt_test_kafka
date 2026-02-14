package pl.wwt.auth.dto;

import java.util.UUID;

public record ProcessResultEvent(
        UUID requestId,
        String result) {}
