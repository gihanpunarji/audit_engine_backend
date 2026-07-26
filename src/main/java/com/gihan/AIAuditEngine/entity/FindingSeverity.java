package com.gihan.AIAuditEngine.entity;

public enum FindingSeverity {
    CRITICAL,  // Immediate action required — possible fraud or major compliance breach
    HIGH,      // Significant anomaly — needs urgent review
    MEDIUM,    // Notable issue — review recommended
    LOW,       // Minor discrepancy — informational
    INFO       // No issue — extracted data point for reference
}
