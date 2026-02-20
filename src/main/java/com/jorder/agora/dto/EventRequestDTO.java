package com.jorder.agora.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record EventRequestDTO(
        @NotBlank String title,
        @NotBlank String description,
        @NotBlank String eventDate,
        @NotNull UUID organizerId
) {
}
