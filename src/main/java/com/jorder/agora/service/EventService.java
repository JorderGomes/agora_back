package com.jorder.agora.service;

import com.jorder.agora.dto.EventRequestDTO;
import com.jorder.agora.dto.EventResponseDTO;
import com.jorder.agora.mapper.EventMapper;
import com.jorder.agora.model.Event;
import com.jorder.agora.repository.EventRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class EventService {

    private final EventRepository eventRepository;
    private final EventMapper eventMapper;

    public EventResponseDTO createEvent(EventRequestDTO eventRequestDTO) {
        Event event = eventMapper.toEntity(eventRequestDTO);
        return eventMapper.toResponseDTO(eventRepository.save(event));
    }

    public EventResponseDTO getEventById(UUID id) {
        return eventRepository.findById(id)
                .map(eventMapper::toResponseDTO)
                .orElseThrow(() -> new EntityNotFoundException("Evento não encontrado"));
    }

    public List<EventResponseDTO> getAllEvents() {
        return eventRepository.findAll().stream()
                .map(eventMapper::toResponseDTO)
                .toList();
    }

    public EventResponseDTO updateEvent(UUID id, EventRequestDTO dto) {
        Event event = eventRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Evento não encontrado"));

        eventMapper.updateEntityFromDto(dto, event);
        return eventMapper.toResponseDTO(eventRepository.save(event));
    }

    public void deleteEvent(UUID id) {
        if (!eventRepository.existsById(id)) {
            throw new EntityNotFoundException("Evento não encontrado");
        }
        eventRepository.deleteById(id);
    }

}
