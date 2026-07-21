package com.gihan.AIAuditEngine.controller;

import com.gihan.AIAuditEngine.dto.AuditTargetRequestDTO;
import com.gihan.AIAuditEngine.dto.AuditTargetResponseDTO;
import com.gihan.AIAuditEngine.service.AuditTargetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/audit-targets")
public class AuditTargetController {

    private final AuditTargetService auditTargetService;

    @Autowired
    public AuditTargetController(AuditTargetService auditTargetService) {
        this.auditTargetService = auditTargetService;
    }

    @PostMapping
    public ResponseEntity<AuditTargetResponseDTO> createAuditTarget(
            @RequestBody AuditTargetRequestDTO dto,
            @AuthenticationPrincipal UserDetails userDetails) {

        AuditTargetResponseDTO response = auditTargetService.createAuditTarget(dto, userDetails.getUsername());
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/organization/{organizationId}")
    public ResponseEntity<List<AuditTargetResponseDTO>> getTargetsByOrganization(
            @PathVariable UUID organizationId) {

        return ResponseEntity.ok(auditTargetService.getTargetsByOrganization(organizationId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<AuditTargetResponseDTO> getTargetById(@PathVariable UUID id) {
        return ResponseEntity.ok(auditTargetService.getTargetById(id));
    }

    @PatchMapping("/{id}/pause")
    public ResponseEntity<AuditTargetResponseDTO> pauseTarget(@PathVariable UUID id) {
        return ResponseEntity.ok(auditTargetService.pauseTarget(id));
    }

    // Reactivate a paused target
    @PatchMapping("/{id}/activate")
    public ResponseEntity<AuditTargetResponseDTO> activateTarget(@PathVariable UUID id) {
        return ResponseEntity.ok(auditTargetService.activateTarget(id));
    }

    // Archive a target — soft delete, keeps data intact
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> archiveTarget(@PathVariable UUID id) {
        auditTargetService.archiveTarget(id);
        return ResponseEntity.noContent().build();
    }
}
