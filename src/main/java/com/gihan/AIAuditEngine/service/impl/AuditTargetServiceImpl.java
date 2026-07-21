package com.gihan.AIAuditEngine.service.impl;

import com.gihan.AIAuditEngine.dto.AuditTargetRequestDTO;
import com.gihan.AIAuditEngine.dto.AuditTargetResponseDTO;
import com.gihan.AIAuditEngine.entity.*;
import com.gihan.AIAuditEngine.repository.AuditTargetRepo;
import com.gihan.AIAuditEngine.repository.OrganizationRepo;
import com.gihan.AIAuditEngine.repository.UserRepo;
import com.gihan.AIAuditEngine.service.AuditTargetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class AuditTargetServiceImpl implements AuditTargetService {

    private final AuditTargetRepo auditTargetRepo;
    private final OrganizationRepo organizationRepo;
    private final UserRepo userRepo;

    @Autowired
    public AuditTargetServiceImpl(AuditTargetRepo auditTargetRepo,
                                  OrganizationRepo organizationRepo,
                                  UserRepo userRepo) {
        this.auditTargetRepo = auditTargetRepo;
        this.organizationRepo = organizationRepo;
        this.userRepo = userRepo;
    }

    @Override
    public AuditTargetResponseDTO createAuditTarget(AuditTargetRequestDTO dto, String createdByEmail) {

        Organization organization = organizationRepo.findById(dto.getOrganizationId())
                .orElseThrow(() -> new RuntimeException("Organization not found with id: " + dto.getOrganizationId()));

        User createdBy = userRepo.findByEmail(createdByEmail);
        if (createdBy == null) {
            throw new RuntimeException("User not found with email: " + createdByEmail);
        }

        if (auditTargetRepo.existsByNameAndOrganizationId(dto.getName(), dto.getOrganizationId())) {
            throw new RuntimeException("An audit target with this name already exists in the organization");
        }

        // 4. Build and save the entity
        AuditTarget auditTarget = new AuditTarget();
        auditTarget.setName(dto.getName());
        auditTarget.setDescription(dto.getDescription());
        auditTarget.setDocumentType(dto.getDocumentType());
        auditTarget.setStatus(AuditTargetStatus.ACTIVE);
        auditTarget.setOrganization(organization);
        auditTarget.setCreatedBy(createdBy);

        AuditTarget saved = auditTargetRepo.save(auditTarget);
        return toResponseDTO(saved);
    }

    @Override
    public List<AuditTargetResponseDTO> getTargetsByOrganization(UUID organizationId) {
        return auditTargetRepo.findByOrganizationId(organizationId)
                .stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    public AuditTargetResponseDTO getTargetById(UUID id) {
        AuditTarget target = auditTargetRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Audit target not found with id: " + id));
        return toResponseDTO(target);
    }

    @Override
    public AuditTargetResponseDTO pauseTarget(UUID id) {
        AuditTarget target = auditTargetRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Audit target not found with id: " + id));
        target.setStatus(AuditTargetStatus.PAUSED);
        return toResponseDTO(auditTargetRepo.save(target));
    }

    @Override
    public AuditTargetResponseDTO activateTarget(UUID id) {
        AuditTarget target = auditTargetRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Audit target not found with id: " + id));
        target.setStatus(AuditTargetStatus.ACTIVE);
        return toResponseDTO(auditTargetRepo.save(target));
    }

    @Override
    public void archiveTarget(UUID id) {
        AuditTarget target = auditTargetRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Audit target not found with id: " + id));
        target.setStatus(AuditTargetStatus.ARCHIVED);
        auditTargetRepo.save(target);
    }

    private AuditTargetResponseDTO toResponseDTO(AuditTarget target) {
        AuditTargetResponseDTO dto = new AuditTargetResponseDTO();
        dto.setId(target.getId());
        dto.setName(target.getName());
        dto.setDescription(target.getDescription());
        dto.setDocumentType(target.getDocumentType());
        dto.setStatus(target.getStatus());
        dto.setCreatedAt(target.getCreatedAt());
        dto.setUpdatedAt(target.getUpdatedAt());

        dto.setOrganizationId(target.getOrganization().getId());
        dto.setOrganizationName(target.getOrganization().getName());

        dto.setCreatedById(target.getCreatedBy().getId());
        dto.setCreatedByEmail(target.getCreatedBy().getEmail());

        return dto;
    }
}
