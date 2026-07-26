package com.gihan.AIAuditEngine.repository;

import com.gihan.AIAuditEngine.entity.AuditFinding;
import com.gihan.AIAuditEngine.entity.FindingSeverity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface AuditFindingRepo extends JpaRepository<AuditFinding, UUID> {

    // All findings for a specific audit
    List<AuditFinding> findByAuditId(UUID auditId);

    // Filter findings by severity for a specific audit
    List<AuditFinding> findByAuditIdAndSeverity(UUID auditId, FindingSeverity severity);
}
