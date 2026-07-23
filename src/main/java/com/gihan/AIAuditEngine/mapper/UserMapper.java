package com.gihan.AIAuditEngine.mapper;

import com.gihan.AIAuditEngine.dto.UserRequestDTO;
import com.gihan.AIAuditEngine.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "organization", ignore = true)
    @Mapping(target = "role", ignore = true)
    User toEntity(UserRequestDTO userRequestDTO);

    UserRequestDTO toDTO(User user);

}
