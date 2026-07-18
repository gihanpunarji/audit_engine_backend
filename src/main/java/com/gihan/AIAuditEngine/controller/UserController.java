package com.gihan.AIAuditEngine.controller;

import com.gihan.AIAuditEngine.dto.UserRequestDTO;
import com.gihan.AIAuditEngine.dto.UserResponseDTO;
import com.gihan.AIAuditEngine.entity.User;
import com.gihan.AIAuditEngine.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/user")
public class UserController {

    @GetMapping("/")
    private String getUsers() {
        return "Hello";
    }
}
