package com.gihan.AIAuditEngine.dto;

import com.gihan.AIAuditEngine.entity.FindingSeverity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuditFindingResponseDTO {
    private UUID id;
    private UUID auditId;
    private String category;
    private String title;
    private String description;
    private String recommendation;
    private FindingSeverity severity;
    private LocalDateTime createdAt;
}
