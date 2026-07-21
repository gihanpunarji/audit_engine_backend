package com.gihan.AIAuditEngine.entity;

public enum AuditTargetStatus {
    ACTIVE,     // Accepting document submissions
    PAUSED,     // Temporarily not accepting submissions
    ARCHIVED    // Decommissioned, kept for records
}
