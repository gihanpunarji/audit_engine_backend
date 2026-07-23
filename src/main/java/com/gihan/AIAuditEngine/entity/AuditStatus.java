package com.gihan.AIAuditEngine.entity;

public enum AuditStatus {
    PENDING,     // File uploaded, waiting to be processed
    PROCESSING,  // AI is currently extracting and validating data
    COMPLETED,   // AI finished, findings are available
    FAILED       // Something went wrong during processing
}
