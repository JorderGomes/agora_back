package com.jorder.agora.controller;

import com.jorder.agora.dto.EventRequestDTO;
import com.jorder.agora.dto.EventResponseDTO;
import com.jorder.agora.dto.UserResponseDTO;
import com.jorder.agora.model.User;
import com.jorder.agora.service.EventService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/events")
@RequiredArgsConstructor
public class EventController {

    private final EventService eventService;

    @PostMapping
    public ResponseEntity<EventResponseDTO> createEvent(@RequestBody EventRequestDTO eventRequest) {
        return ResponseEntity.ok(eventService.createEvent(eventRequest));
    }

    @GetMapping("/{id}")
    public ResponseEntity<EventResponseDTO> getEventById(@PathVariable UUID id) {
        return ResponseEntity.ok(eventService.getEventById(id));
    }

    @GetMapping
    public ResponseEntity<List<EventResponseDTO>> getAllEvents() {
        return ResponseEntity.ok(eventService.getAllEvents());
    }

    @GetMapping("/search")
    public ResponseEntity<List<EventResponseDTO>> searchEvents(
            @RequestParam(required = false) String title,
            @RequestParam(required = false) UUID organizerId) {
        return ResponseEntity.ok(eventService.searchEvents(title, organizerId));
    }

    @PutMapping("/{id}")
    public ResponseEntity<EventResponseDTO> updateEvent(@PathVariable UUID id, @RequestBody EventRequestDTO eventRequest) {
        return ResponseEntity.ok(eventService.updateEvent(id, eventRequest));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEvent(@PathVariable UUID id, @RequestParam UUID organizerId) {
        eventService.deleteEvent(id, organizerId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{eventId}/participants")
    public ResponseEntity<List<UserResponseDTO>> getEventParticipants(@PathVariable UUID eventId) {
        return ResponseEntity.ok(eventService.getEventParticipants(eventId));
    }

    @PostMapping("/{eventId}/participants/{userId}")
    public ResponseEntity<Void> registerParticipant(@PathVariable UUID eventId, @PathVariable UUID userId) {
        eventService.registerParticipant(eventId, userId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @DeleteMapping("/{eventId}/participants/{userId}")
    public ResponseEntity<Void> unregisterParticipant(@PathVariable UUID eventId, @PathVariable UUID userId) {
        eventService.unregisterParticipant(eventId, userId);
        return ResponseEntity.noContent().build();
    }

}
