package com.jorder.agora.service;

import com.jorder.agora.dto.EventRequestDTO;
import com.jorder.agora.dto.EventResponseDTO;
import com.jorder.agora.dto.UserResponseDTO;
import com.jorder.agora.mapper.EventMapper;
import com.jorder.agora.mapper.UserMapper;
import com.jorder.agora.model.Event;
import com.jorder.agora.model.User;
import com.jorder.agora.repository.EventRepository;
import com.jorder.agora.repository.EventSpecification;
import com.jorder.agora.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class EventService {

    private final EventRepository eventRepository;
    private final UserRepository userRepository;
    private final EventMapper eventMapper;
    private final UserMapper userMapper;

    public EventResponseDTO createEvent(EventRequestDTO eventRequestDTO) {
        User organizer = userRepository.findById(eventRequestDTO.organizerId())
                .orElseThrow(() -> new EntityNotFoundException("Organizador não encontrado"));

        Event event = eventMapper.toEntity(eventRequestDTO);
        event.setOrganizer(organizer);
        Event savedEvent = eventRepository.save(event);

        return eventMapper.toResponseDTO(savedEvent);
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

    public List<EventResponseDTO> searchEvents(String title, UUID organizerId) {
        Specification<Event> spec = Specification
                .where(EventSpecification.hasTitle(title))
                .and(EventSpecification.hasOrganizer(organizerId));

        return eventRepository.findAll(spec).stream()
                .map(eventMapper::toResponseDTO)
                .toList();
    }

    public EventResponseDTO updateEvent(UUID id, EventRequestDTO dto) {
        Event event = eventRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Evento não encontrado"));

        if (!event.getOrganizer().getId().equals(dto.organizerId())) {
            throw new RuntimeException("Apenas o organizador do evento pode realizar alterações.");
        }

        eventMapper.updateEntityFromDto(dto, event);
        return eventMapper.toResponseDTO(eventRepository.save(event));
    }

    public void deleteEvent(UUID id, UUID organizerId) {
        Event event = eventRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Evento não encontrado"));

        if (!event.getOrganizer().getId().equals(organizerId)) {
            throw new RuntimeException("Ação não permitida: Você não é o organizador deste evento.");
        }

        eventRepository.deleteById(id);
    }

    public List<UserResponseDTO> getEventParticipants(UUID eventId) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new EntityNotFoundException("Evento não encontrado"));

        // Converte a lista de entidades para DTOs usando o mapper
        return event.getParticipants().stream()
                .map(userMapper::toResponseDTO)
                .toList();
    }

    @Transactional
    public void registerParticipant(UUID eventId, UUID userId) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new EntityNotFoundException("Evento não encontrado"));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("Usuário não encontrado"));
        // TODO: Quando o evento tiver status ele deve ser verificado aqui
        if (event.getParticipants().contains(user)) {
            throw new RuntimeException("Usuário já está inscrito neste evento.");
        }

        user.getEventsRegistered().add(event);
        event.getParticipants().add(user);

        userRepository.save(user);
    }

    @Transactional
    public void unregisterParticipant(UUID eventId, UUID userId) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new EntityNotFoundException("Evento não encontrado"));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("Usuário não encontrado"));

        user.getEventsRegistered().remove(event);
        event.getParticipants().remove(user);

        userRepository.save(user);
    }

}
