package com.jorder.agora.service;

import com.jorder.agora.dto.UserResponseDTO;
import com.jorder.agora.mapper.EventMapper;
import com.jorder.agora.mapper.UserMapper;
import com.jorder.agora.model.Event;
import com.jorder.agora.model.User;
import com.jorder.agora.repository.EventRepository;
import com.jorder.agora.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RegisterService {

    private final EventRepository eventRepository;
    private final UserRepository userRepository;
    private final EventMapper eventMapper;
    private final UserMapper userMapper;


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
            throw new RuntimeException("Usuário já está inscrito neste evento."); // TODO: Business exception com status code 409 Conflict
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

        if (!event.getParticipants().contains(user)) {
            throw new RuntimeException("O usuário informado não está inscrito neste evento."); // TODO: Business exception com status code 409 Conflict
        }

        user.getEventsRegistered().remove(event);
        event.getParticipants().remove(user);

        userRepository.save(user);
    }


}
