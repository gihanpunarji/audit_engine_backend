package com.gihan.AIAuditEngine.controller;

import com.gihan.AIAuditEngine.dto.AuditResponseDTO;
import com.gihan.AIAuditEngine.service.AuditService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/audits")
public class AuditController {

    private final AuditService auditService;

    @Autowired
    public AuditController(AuditService auditService) {
        this.auditService = auditService;
    }

    @PostMapping(value = "/submit", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<AuditResponseDTO> submitDocument(
            @RequestParam("auditTargetId") UUID auditTargetId,
            @RequestParam("file") MultipartFile file,
            @AuthenticationPrincipal UserDetails userDetails) {

        AuditResponseDTO response = auditService.submitDocument(
                auditTargetId, file, userDetails.getUsername());

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/target/{auditTargetId}")
    public ResponseEntity<List<AuditResponseDTO>> getAuditsByTarget(
            @PathVariable UUID auditTargetId) {

        return ResponseEntity.ok(auditService.getAuditsByTarget(auditTargetId));
    }

    @GetMapping("/{auditId}")
    public ResponseEntity<AuditResponseDTO> getAuditById(@PathVariable UUID auditId) {
        return ResponseEntity.ok(auditService.getAuditById(auditId));
    }
}
