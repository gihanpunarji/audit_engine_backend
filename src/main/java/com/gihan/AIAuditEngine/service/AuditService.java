package com.gihan.AIAuditEngine.service;

import com.gihan.AIAuditEngine.dto.AuditResponseDTO;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

public interface AuditService {

    AuditResponseDTO submitDocument(UUID auditTargetId, MultipartFile file, String submittedByEmail);

    List<AuditResponseDTO> getAuditsByTarget(UUID auditTargetId);

    AuditResponseDTO getAuditById(UUID auditId);
}
