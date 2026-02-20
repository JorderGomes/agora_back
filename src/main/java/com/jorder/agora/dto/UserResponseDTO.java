package com.jorder.agora.dto;

import java.util.UUID;

public record UserResponseDTO(
        UUID id,
        String name,
        String email
) {
}
