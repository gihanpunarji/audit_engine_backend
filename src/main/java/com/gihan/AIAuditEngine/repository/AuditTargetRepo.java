package com.gihan.AIAuditEngine.repository;

import com.gihan.AIAuditEngine.entity.AuditTarget;
import com.gihan.AIAuditEngine.entity.AuditTargetStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface AuditTargetRepo extends JpaRepository<AuditTarget, UUID> {

    // All targets for a given organization
    List<AuditTarget> findByOrganizationId(UUID organizationId);

    // Targets filtered by status within an organization
    List<AuditTarget> findByOrganizationIdAndStatus(UUID organizationId, AuditTargetStatus status);

    // Check for duplicate name within the same org
    boolean existsByNameAndOrganizationId(String name, UUID organizationId);
}
