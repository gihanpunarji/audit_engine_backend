package com.gihan.AIAuditEngine.service;

import com.gihan.AIAuditEngine.dto.OrganizationRequestDTO;
import com.gihan.AIAuditEngine.entity.Organization;

import java.util.List;
import java.util.UUID;

public interface OrganizationService {
    String createOrganization(OrganizationRequestDTO dto);
    List<Organization> getAllOrganizations();
    Organization getOrganizationById(UUID id);
}
