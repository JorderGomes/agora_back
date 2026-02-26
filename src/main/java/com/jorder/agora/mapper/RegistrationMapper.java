package com.jorder.agora.mapper;

import com.jorder.agora.dto.ParticipantResponseDTO;
import com.jorder.agora.model.Registration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface RegistrationMapper {

    @Mapping(source = "user.id", target = "id")
    @Mapping(source = "user.name", target = "name")
    @Mapping(source = "user.email", target = "email")
    @Mapping(source = "present", target = "present")
    ParticipantResponseDTO toParticipantDTO(Registration registration);

}
