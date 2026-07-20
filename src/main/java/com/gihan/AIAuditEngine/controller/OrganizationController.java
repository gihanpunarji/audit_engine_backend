package com.gihan.AIAuditEngine.controller;

import com.gihan.AIAuditEngine.dto.OrganizationRequestDTO;
import com.gihan.AIAuditEngine.entity.Organization;
import com.gihan.AIAuditEngine.service.OrganizationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/organizations")
public class OrganizationController {

    private final OrganizationService organizationService;

    @Autowired
    public OrganizationController(OrganizationService organizationService) {
        this.organizationService = organizationService;
    }

    @PostMapping("/create")
    public String createOrganization(@RequestBody OrganizationRequestDTO dto) {
        return organizationService.createOrganization(dto);
    }

    @GetMapping
    public List<Organization> getAllOrganizations() {
        return organizationService.getAllOrganizations();
    }

    @GetMapping("/{id}")
    public Organization getOrganizationById(@PathVariable UUID id) {
        return organizationService.getOrganizationById(id);
    }
}
