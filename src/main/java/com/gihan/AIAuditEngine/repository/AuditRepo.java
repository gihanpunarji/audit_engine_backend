package com.gihan.AIAuditEngine.repository;

import com.gihan.AIAuditEngine.entity.Audit;
import com.gihan.AIAuditEngine.entity.AuditStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface AuditRepo extends JpaRepository<Audit, UUID> {

    @Query("SELECT a FROM Audit a JOIN FETCH a.auditTarget t JOIN FETCH t.organization WHERE a.id = :id")
    Optional<Audit> findByIdWithTargetAndOrganization(@Param("id") UUID id);

    // All audits for a given audit target
    List<Audit> findByAuditTargetId(UUID auditTargetId);

    // Filter by status — useful to find all PENDING audits for the processor to pick up
    List<Audit> findByStatus(AuditStatus status);

    // All audits submitted by a specific user
    List<Audit> findBySubmittedById(UUID userId);
}
