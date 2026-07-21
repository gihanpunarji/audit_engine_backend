package com.gihan.AIAuditEngine.service;

import com.gihan.AIAuditEngine.dto.AuditTargetRequestDTO;
import com.gihan.AIAuditEngine.dto.AuditTargetResponseDTO;

import java.util.List;
import java.util.UUID;

public interface AuditTargetService {

    AuditTargetResponseDTO createAuditTarget(AuditTargetRequestDTO dto, String createdByEmail);

    List<AuditTargetResponseDTO> getTargetsByOrganization(UUID organizationId);

    AuditTargetResponseDTO getTargetById(UUID id);

    AuditTargetResponseDTO pauseTarget(UUID id);

    AuditTargetResponseDTO activateTarget(UUID id);

    void archiveTarget(UUID id);
}
