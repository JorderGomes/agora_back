package com.jorder.agora.dto;

public record EventResponseDTO(
        String id,
        String title,
        String description,
        String eventDate,
        UserResponseDTO organizer,
        int participantCount
) {
}
