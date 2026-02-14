package pl.wwt.data.dto;

import jakarta.validation.constraints.Size;

public record ProcessRequest(@Size(min = 1) String text) {}
