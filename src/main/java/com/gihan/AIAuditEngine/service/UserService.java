package com.gihan.AIAuditEngine.service;

import com.gihan.AIAuditEngine.dto.UserRequestDTO;
import com.gihan.AIAuditEngine.entity.User;

public interface UserService {
    String createUser(UserRequestDTO userRequestDTO);

    User getUsers(String email);

    String verifyUser(UserRequestDTO userRequestDTO);
}
