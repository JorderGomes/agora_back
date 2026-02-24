package com.jorder.agora.controller;

import com.jorder.agora.dto.UserResponseDTO;
import com.jorder.agora.service.RegisterService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/registers")
@RequiredArgsConstructor
public class RegisterController {

    private final RegisterService registerService;

    @GetMapping("/{eventId}/participants")
    public ResponseEntity<List<UserResponseDTO>> getEventParticipants(@PathVariable UUID eventId) {
        return ResponseEntity.ok(registerService.getEventParticipants(eventId));
    }

    @PostMapping("/{eventId}/participants/{userId}")
    public ResponseEntity<Void> registerParticipant(@PathVariable UUID eventId, @PathVariable UUID userId) {
        registerService.registerParticipant(eventId, userId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @DeleteMapping("/{eventId}/participants/{userId}")
    public ResponseEntity<Void> unregisterParticipant(@PathVariable UUID eventId, @PathVariable UUID userId) {
        registerService.unregisterParticipant(eventId, userId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{eventId}/participants/{userId}/checkin")
    public ResponseEntity<Void> checkInParticipant(@PathVariable UUID eventId, @PathVariable UUID userId, @RequestParam boolean present) {
        registerService.updatePresence(eventId, userId, present);
        return ResponseEntity.noContent().build();
    }
}
