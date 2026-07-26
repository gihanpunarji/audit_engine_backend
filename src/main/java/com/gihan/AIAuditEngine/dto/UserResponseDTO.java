package com.gihan.AIAuditEngine.dto;

import com.gihan.AIAuditEngine.entity.UserRole;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserResponseDTO {
    private UUID id;
    private String fName;
    private String lName;
    private String email;
    private UserRole role;
    private UUID organizationId;
    private String organizationName;
}
