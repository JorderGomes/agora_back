package com.jorder.agora.service;

import com.jorder.agora.dto.ParticipantResponseDTO;
import com.jorder.agora.dto.UserResponseDTO;
//import com.jorder.agora.mapper.EventMapper;
import com.jorder.agora.exceptions.BusinessException;
import com.jorder.agora.mapper.RegistrationMapper;
import com.jorder.agora.mapper.UserMapper;
import com.jorder.agora.model.Event;
import com.jorder.agora.model.Registration;
import com.jorder.agora.model.User;
import com.jorder.agora.repository.EventRepository;
import com.jorder.agora.repository.RegistrationRepository;
import com.jorder.agora.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
//import org.antlr.v4.runtime.misc.LogManager;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RegisterService {

    private final EventRepository eventRepository;
    private final UserRepository userRepository;
    private final RegistrationRepository registrationRepository;
    private final RegistrationMapper registrationMapper;

    public List<ParticipantResponseDTO> getEventParticipants(UUID eventId, Boolean present) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new EntityNotFoundException("Evento não encontrado"));

        return event.getRegistrations().stream()
                .filter(reg -> present == null || reg.isPresent() == present)
                .map(registrationMapper::toParticipantDTO)
                .toList();
    }

    @Transactional
    public void registerParticipant(UUID eventId, UUID userId) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new EntityNotFoundException("Evento não encontrado"));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("Usuário não encontrado"));

        if (registrationRepository.existsByEventIdAndUserId(eventId, userId)) {
            throw new BusinessException("Usuário já está inscrito neste evento.");
        }

        Registration registration = new Registration();
        registration.setEvent(event);
        registration.setUser(user);
        registration.setPresent(false);
        registration.setNotificationSent(false);

        registrationRepository.save(registration);
    }

    @Transactional
    public void unregisterParticipant(UUID eventId, UUID userId) {
        Registration registration = registrationRepository.findByEventIdAndUserId(eventId, userId)
                .orElseThrow(() -> new BusinessException("O usuário informado não está inscrito neste evento."));

        registrationRepository.delete(registration);
    }

    @Transactional
    public void updatePresence(UUID eventId, UUID userId, boolean presence) {
        Registration registration = registrationRepository.findByEventIdAndUserId(eventId, userId)
                .orElseThrow(() -> new EntityNotFoundException("Inscrição não encontrada para este usuário neste evento."));

        registration.setPresent(presence);

        registrationRepository.save(registration);
    }



}
