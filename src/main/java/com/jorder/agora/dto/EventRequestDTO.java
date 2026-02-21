package com.jorder.agora.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;
import java.util.UUID;

public record EventRequestDTO(
        @NotBlank String title,
        @NotBlank String description,
        @NotBlank LocalDateTime eventDate,
        @NotNull UUID organizerId
) {
}
