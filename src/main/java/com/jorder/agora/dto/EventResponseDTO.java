package com.jorder.agora.dto;

import java.time.LocalDateTime;
import java.util.UUID;

public record EventResponseDTO(
        UUID id,
        String title,
        String description,
        LocalDateTime eventDate,
        UserResponseDTO organizer,
        int participantCount
) {
}
