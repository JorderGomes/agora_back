package com.jorder.agora.service;

import com.jorder.agora.dto.ParticipantResponseDTO;
import com.jorder.agora.exceptions.BusinessException;
import com.jorder.agora.mapper.RegistrationMapper;
import com.jorder.agora.model.Event;
import com.jorder.agora.model.Registration;
import com.jorder.agora.model.User;
import com.jorder.agora.repository.EventRepository;
import com.jorder.agora.repository.RegistrationRepository;
import com.jorder.agora.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RegisterServiceTest {

    @Mock
    private RegistrationRepository registrationRepository;
    @Mock
    private EventRepository eventRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private RegistrationMapper registrationMapper;

    @InjectMocks
    private RegisterService registerService;

    @Test
    @DisplayName("Não deve permitir inscrição duplicada no mesmo evento")
    void shouldNotRegisterTwice() {
        UUID eventId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();

        when(eventRepository.findById(eventId)).thenReturn(Optional.of(new Event()));
        when(userRepository.findById(userId)).thenReturn(Optional.of(new User()));
        // Simula que já existe a inscrição no banco
        when(registrationRepository.existsByEventIdAndUserId(eventId, userId)).thenReturn(true);

        assertThrows(BusinessException.class, () -> registerService.registerParticipant(eventId, userId));
        verify(registrationRepository, never()).save(any());
    }

    @Test
    @DisplayName("Deve confirmar presença com sucesso")
    void confirmPresenceSuccess() {
        UUID eventId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();
        Registration reg = new Registration();
        reg.setPresent(false);

        when(registrationRepository.findByEventIdAndUserId(eventId, userId)).thenReturn(Optional.of(reg));

        registerService.updatePresence(eventId, userId, true);

        assertTrue(reg.isPresent());
        verify(registrationRepository).save(reg);
    }

    @Test
    @DisplayName("Deve buscar participantes filtrando por presença")
    void getEventParticipantsWithFilter() {
        UUID eventId = UUID.randomUUID();
        Event event = new Event();

        Registration reg1 = new Registration();
        reg1.setPresent(true);
        Registration reg2 = new Registration();
        reg2.setPresent(false);

        event.setRegistrations(List.of(reg1, reg2));

        when(eventRepository.findById(eventId)).thenReturn(Optional.of(event));
        when(registrationMapper.toParticipantDTO(any())).thenReturn(mock(ParticipantResponseDTO.class));

        // Testando filtro: apenas presentes
        List<ParticipantResponseDTO> presentOnly = registerService.getEventParticipants(eventId, true);
        assertEquals(1, presentOnly.size());

        // Testando filtro: todos (null)
        List<ParticipantResponseDTO> all = registerService.getEventParticipants(eventId, null);
        assertEquals(2, all.size());
    }

    @Test
    @DisplayName("Deve registrar participante com sucesso")
    void registerParticipant() {
        UUID eventId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();

        when(eventRepository.findById(eventId)).thenReturn(Optional.of(new Event()));
        when(userRepository.findById(userId)).thenReturn(Optional.of(new User()));
        when(registrationRepository.existsByEventIdAndUserId(eventId, userId)).thenReturn(false);

        assertDoesNotThrow(() -> registerService.registerParticipant(eventId, userId));

        verify(registrationRepository, times(1)).save(any(Registration.class));
    }

    @Test
    @DisplayName("Deve remover inscrição com sucesso")
    void unregisterParticipantSuccess() {
        UUID eventId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();
        Registration registration = new Registration();

        when(registrationRepository.findByEventIdAndUserId(eventId, userId))
                .thenReturn(Optional.of(registration));

        registerService.unregisterParticipant(eventId, userId);

        verify(registrationRepository).delete(registration);
    }

    @Test
    @DisplayName("Deve lançar exceção ao tentar remover inscrição inexistente")
    void unregisterParticipantNotFound() {
        UUID eventId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();

        when(registrationRepository.findByEventIdAndUserId(eventId, userId))
                .thenReturn(Optional.empty());

        assertThrows(BusinessException.class, () -> registerService.unregisterParticipant(eventId, userId));
    }

    @Test
    @DisplayName("Deve lançar exceção ao buscar participantes de evento inexistente")
    void getEventParticipantsNotFound() {
        UUID eventId = UUID.randomUUID();
        when(eventRepository.findById(eventId)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> registerService.getEventParticipants(eventId, null));
    }
}