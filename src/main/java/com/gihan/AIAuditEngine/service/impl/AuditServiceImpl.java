package com.gihan.AIAuditEngine.service.impl;

import com.gihan.AIAuditEngine.dto.AuditResponseDTO;
import com.gihan.AIAuditEngine.entity.*;
import com.gihan.AIAuditEngine.repository.AuditRepo;
import com.gihan.AIAuditEngine.repository.AuditTargetRepo;
import com.gihan.AIAuditEngine.repository.UserRepo;
import com.gihan.AIAuditEngine.service.AuditService;
import com.gihan.AIAuditEngine.service.S3Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class AuditServiceImpl implements AuditService {

    private final AuditRepo auditRepo;
    private final AuditTargetRepo auditTargetRepo;
    private final UserRepo userRepo;
    private final S3Service s3Service;

    @Autowired
    public AuditServiceImpl(AuditRepo auditRepo,
                            AuditTargetRepo auditTargetRepo,
                            UserRepo userRepo,
                            S3Service s3Service) {
        this.auditRepo = auditRepo;
        this.auditTargetRepo = auditTargetRepo;
        this.userRepo = userRepo;
        this.s3Service = s3Service;
    }

    @Override
    public AuditResponseDTO submitDocument(UUID auditTargetId, MultipartFile file, String submittedByEmail) {

        AuditTarget auditTarget = auditTargetRepo.findById(auditTargetId)
                .orElseThrow(() -> new RuntimeException("Audit target not found: " + auditTargetId));

        if (auditTarget.getStatus() != AuditTargetStatus.ACTIVE) {
            throw new RuntimeException("Audit target is not active. Current status: " + auditTarget.getStatus());
        }

        User submittedBy = userRepo.findByEmail(submittedByEmail);
        if (submittedBy == null) {
            throw new RuntimeException("User not found: " + submittedByEmail);
        }

        // Use a UUID purely for the S3 path — do NOT set it as the entity ID
        UUID pathId = UUID.randomUUID();
        String s3Key = String.format("documents/%s/%s/%s/%s",
                auditTarget.getOrganization().getId(),
                auditTargetId,
                pathId,
                file.getOriginalFilename());

        s3Service.uploadFile(s3Key, file);

        // Persist the Audit — let JPA generate the ID via @GeneratedValue
        Audit audit = new Audit();
        // audit.setId() is NOT called — JPA assigns the ID on persist()
        audit.setAuditTarget(auditTarget);
        audit.setSubmittedBy(submittedBy);
        audit.setOriginalFileName(file.getOriginalFilename());
        audit.setS3Key(s3Key);
        audit.setStatus(AuditStatus.PENDING);

        Audit saved = auditRepo.save(audit);
        return toResponseDTO(saved);
    }

    @Override
    public List<AuditResponseDTO> getAuditsByTarget(UUID auditTargetId) {
        return auditRepo.findByAuditTargetId(auditTargetId)
                .stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    public AuditResponseDTO getAuditById(UUID auditId) {
        Audit audit = auditRepo.findById(auditId)
                .orElseThrow(() -> new RuntimeException("Audit not found: " + auditId));
        return toResponseDTO(audit);
    }

    // --- Private mapper helper ---
    private AuditResponseDTO toResponseDTO(Audit audit) {
        AuditResponseDTO dto = new AuditResponseDTO();
        dto.setId(audit.getId());
        dto.setStatus(audit.getStatus());
        dto.setOriginalFileName(audit.getOriginalFileName());
        dto.setS3Key(audit.getS3Key());
        dto.setRawExtractedData(audit.getRawExtractedData());
        dto.setFailureReason(audit.getFailureReason());
        dto.setSubmittedAt(audit.getSubmittedAt());
        dto.setProcessedAt(audit.getProcessedAt());

        // Flatten audit target
        dto.setAuditTargetId(audit.getAuditTarget().getId());
        dto.setAuditTargetName(audit.getAuditTarget().getName());

        // Flatten submitter
        dto.setSubmittedById(audit.getSubmittedBy().getId());
        dto.setSubmittedByEmail(audit.getSubmittedBy().getEmail());

        return dto;
    }
}
