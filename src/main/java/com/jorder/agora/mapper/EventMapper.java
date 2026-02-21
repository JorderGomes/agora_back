package com.jorder.agora.mapper;

import com.jorder.agora.dto.EventRequestDTO;
import com.jorder.agora.dto.EventResponseDTO;
import com.jorder.agora.model.Event;
import org.mapstruct.*;

@Mapper(componentModel = "spring", uses = {UserMapper.class})
public interface EventMapper {

    @Mapping(source = "organizerId", target = "organizer.id")
    Event toEntity(EventRequestDTO dto);

    @Mapping(target = "participantCount", expression = "java(mapParticipantCount(event))")
    EventResponseDTO toResponseDTO(Event event);

    default Integer mapParticipantCount(Event event) {
        if (event.getParticipants() == null) {
            return 0;
        }
        return event.getParticipants().size();
    }

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true) // Segurança: não deixa o ID ser alterado via DTO
    @Mapping(target = "organizer", ignore = true) // Segurança: o dono do evento não muda no update
    void updateEntityFromDto(EventRequestDTO dto, @MappingTarget Event entity);

}
