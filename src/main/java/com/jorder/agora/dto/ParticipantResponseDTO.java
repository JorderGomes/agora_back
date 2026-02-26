package com.jorder.agora.dto;

import java.util.UUID;

public record ParticipantResponseDTO(
        UUID id,
        String name,
        String email,
        boolean present
) {
}
