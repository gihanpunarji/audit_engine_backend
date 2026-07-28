package com.gihan.AIAuditEngine.controller;

import com.gihan.AIAuditEngine.dto.UserRequestDTO;
import com.gihan.AIAuditEngine.dto.UserResponseDTO;
import com.gihan.AIAuditEngine.entity.User;
import com.gihan.AIAuditEngine.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final UserService userService;

    @Autowired
    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/create")
    public String createUser(@RequestBody UserRequestDTO userRequestDTO) {
        return userService.createUser(userRequestDTO);
    }

    @PostMapping("/login")
    public String loginUser(@RequestBody UserRequestDTO userRequestDTO) {
        return userService.verifyUser(userRequestDTO);
    }

    @GetMapping("/me")
    public ResponseEntity<UserResponseDTO> getCurrentUser(@AuthenticationPrincipal UserDetails userDetails) {
        User user = userService.getUsers(userDetails.getUsername());
        if (user == null) {
            return ResponseEntity.notFound().build();
        }
        UserResponseDTO dto = new UserResponseDTO();
        dto.setId(user.getId());
        dto.setFName(user.getFName());
        dto.setLName(user.getLName());
        dto.setEmail(user.getEmail());
        dto.setRole(user.getRole());
        if (user.getOrganization() != null) {
            dto.setOrganizationId(user.getOrganization().getId());
            dto.setOrganizationName(user.getOrganization().getName());
        }
        return ResponseEntity.ok(dto);
    }
}
