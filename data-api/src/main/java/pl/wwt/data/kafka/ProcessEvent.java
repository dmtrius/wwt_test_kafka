package pl.wwt.data.kafka;

import java.util.UUID;

public record ProcessEvent(
        UUID requestId,
        UUID userId,
        String text) {
}
