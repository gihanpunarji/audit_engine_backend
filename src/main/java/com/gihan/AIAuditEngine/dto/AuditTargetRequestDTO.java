package com.gihan.AIAuditEngine.dto;

import com.gihan.AIAuditEngine.entity.DocumentType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuditTargetRequestDTO {

    private String name;
    private String description;
    private DocumentType documentType;
    private UUID organizationId;
}
