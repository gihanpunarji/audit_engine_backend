package com.gihan.AIAuditEngine.dto;

import com.gihan.AIAuditEngine.entity.AuditTargetStatus;
import com.gihan.AIAuditEngine.entity.DocumentType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuditTargetResponseDTO {

    private UUID id;
    private String name;
    private String description;
    private DocumentType documentType;
    private AuditTargetStatus status;

    // Flat fields — avoids exposing full nested entities in API responses
    private UUID organizationId;
    private String organizationName;
    private UUID createdById;
    private String createdByEmail;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
