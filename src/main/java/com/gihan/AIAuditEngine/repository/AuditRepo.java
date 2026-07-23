package com.gihan.AIAuditEngine.repository;

import com.gihan.AIAuditEngine.entity.Audit;
import com.gihan.AIAuditEngine.entity.AuditStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface AuditRepo extends JpaRepository<Audit, UUID> {

    // All audits for a given audit target
    List<Audit> findByAuditTargetId(UUID auditTargetId);

    // Filter by status — useful to find all PENDING audits for the processor to pick up
    List<Audit> findByStatus(AuditStatus status);

    // All audits submitted by a specific user
    List<Audit> findBySubmittedById(UUID userId);
}
