package com.gihan.AIAuditEngine.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "audit_findings")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuditFinding {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    // The audit run this finding belongs to
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "audit_id", nullable = false)
    private Audit audit;

    // e.g. "MISSING_FIELD", "AMOUNT_MISMATCH", "INVALID_DATE", "SUSPICIOUS_VENDOR", "POLICY_VIOLATION"
    @Column(name = "category", nullable = false)
    private String category;

    // Short title summarizing the finding
    @Column(name = "title", nullable = false)
    private String title;

    // Detailed description from the AI
    @Column(name = "description", columnDefinition = "TEXT", nullable = false)
    private String description;

    // What the AI recommends doing about it
    @Column(name = "recommendation", columnDefinition = "TEXT")
    private String recommendation;

    @Enumerated(EnumType.STRING)
    @Column(name = "severity", nullable = false)
    private FindingSeverity severity;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();
}
