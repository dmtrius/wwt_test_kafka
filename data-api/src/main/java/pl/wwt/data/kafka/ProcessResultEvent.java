package pl.wwt.data.kafka;

import java.util.UUID;

public record ProcessResultEvent(
        UUID requestId,
        String result) {}
