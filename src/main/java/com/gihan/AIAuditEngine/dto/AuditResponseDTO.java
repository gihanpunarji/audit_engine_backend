package com.gihan.AIAuditEngine.dto;

import com.gihan.AIAuditEngine.entity.AuditStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuditResponseDTO {

    private UUID id;
    private AuditStatus status;
    private String originalFileName;
    private String s3Key;

    // Flattened — avoids exposing full nested entities
    private UUID auditTargetId;
    private String auditTargetName;
    private UUID submittedById;
    private String submittedByEmail;

    private String rawExtractedData;
    private String failureReason;

    private LocalDateTime submittedAt;
    private LocalDateTime processedAt;
}
