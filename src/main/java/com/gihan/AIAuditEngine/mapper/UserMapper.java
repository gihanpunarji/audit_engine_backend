package com.gihan.AIAuditEngine.mapper;

import com.gihan.AIAuditEngine.dto.UserRequestDTO;
import com.gihan.AIAuditEngine.entity.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {

    User toEntity(UserRequestDTO userRequestDTO);

    UserRequestDTO toDTO(User user);

}
