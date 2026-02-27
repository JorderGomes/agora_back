package com.jorder.agora.service;

import com.jorder.agora.dto.EventRequestDTO;
import com.jorder.agora.dto.EventResponseDTO;
import com.jorder.agora.dto.UserResponseDTO;
import com.jorder.agora.mapper.EventMapper;
import com.jorder.agora.model.Event;
import com.jorder.agora.model.User;
import com.jorder.agora.repository.EventRepository;
import com.jorder.agora.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.hibernate.validator.constraints.UniqueElements;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EventServiceTest {

    @Mock
    private EventRepository eventRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private EventMapper eventMapper;

    @InjectMocks
    private EventService eventService;

    @Test
    @DisplayName("Deve criar evento associando um organizador existente")
    void createEventSuccess() {
        UUID orgId = UUID.randomUUID();
        EventRequestDTO dto = new EventRequestDTO("Poker Night", "Desc", LocalDateTime.now(), orgId);
        User organizer = new User();
        Event event = new Event();

        when(userRepository.findById(orgId)).thenReturn(Optional.of(organizer));
        when(eventMapper.toEntity(dto)).thenReturn(event);
        when(eventRepository.save(event)).thenReturn(event);

        eventService.createEvent(dto);

        verify(userRepository).findById(orgId);
        verify(eventRepository).save(event);
    }

    @Test
    @DisplayName("Deve buscar evento por ID com sucesso")
    void getEventByIdSuccess() {
        UUID id = UUID.randomUUID();
        Event event = new Event();
        UserResponseDTO userResponse = new UserResponseDTO(UUID.randomUUID(), "Organizer", "org@email.com");
        EventResponseDTO expected = new EventResponseDTO(id, "Title", "Desc", LocalDateTime.now(), userResponse, 0);

        when(eventRepository.findById(id)).thenReturn(Optional.of(event));
        when(eventMapper.toResponseDTO(event)).thenReturn(expected);

        EventResponseDTO result = eventService.getEventById(id);

        assertNotNull(result);
        assertEquals(id, result.id());
        verify(eventRepository).findById(id);
    }

    @Test
    @DisplayName("Deve lançar exceção ao buscar evento inexistente")
    void getEventByIdNotFound() {
        UUID id = UUID.randomUUID();
        when(eventRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> eventService.getEventById(id));
    }

    @Test
    @DisplayName("Deve retornar todos os eventos")
    void getAllEventsSuccess() {
        Event event = new Event();
        when(eventRepository.findAll()).thenReturn(List.of(event));
        when(eventMapper.toResponseDTO(event)).thenReturn(mock(EventResponseDTO.class));

        List<EventResponseDTO> result = eventService.getAllEvents();

        assertEquals(1, result.size());
        verify(eventRepository).findAll();
    }

    @Test
    @DisplayName("Deve buscar eventos por filtros (Specification)")
    void searchEventsSuccess() {
        Event event = new Event();
        when(eventRepository.findAll(any(Specification.class))).thenReturn(List.of(event));

        eventService.searchEvents("Title", UUID.randomUUID());

        verify(eventRepository).findAll(any(Specification.class));
    }

    @Test
    @DisplayName("Deve atualizar evento com sucesso")
    void updateEventSuccess() {
        UUID id = UUID.randomUUID();
        EventRequestDTO dto = new EventRequestDTO("New Title", "New Desc", LocalDateTime.now(), UUID.randomUUID());
        Event event = new Event();
        User organizer = new User();
        organizer.setId(dto.organizerId());
        event.setOrganizer(organizer);

        when(eventRepository.findById(id)).thenReturn(Optional.of(event));
        when(eventRepository.save(event)).thenReturn(event);

        eventService.updateEvent(id, dto);

        verify(eventMapper).updateEntityFromDto(dto, event);
        verify(eventRepository).save(event);
    }

    @Test
    @DisplayName("Deve deletar evento com sucesso")
    void deleteEventSuccess() {
        UUID id = UUID.randomUUID();
        UUID orgId = UUID.randomUUID();
        Event event = new Event();
        User organizer = new User();
        organizer.setId(orgId);
        event.setOrganizer(organizer);
        when(eventRepository.findById(id)).thenReturn(Optional.of(event));

        eventService.deleteEvent(id, orgId);

        verify(eventRepository).deleteById(id);
    }

    @Test
    @DisplayName("Deve lançar exceção ao deletar evento inexistente")
    void deleteEventNotFound() {
        UUID id = UUID.randomUUID();
        UUID orgId = UUID.randomUUID();
//        when(eventRepository.existsById(id)).thenReturn(false);

        assertThrows(EntityNotFoundException.class, () -> eventService.deleteEvent(id, orgId));
        verify(eventRepository, never()).deleteById(id);
    }

}