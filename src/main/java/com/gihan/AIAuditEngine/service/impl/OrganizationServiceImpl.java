package com.gihan.AIAuditEngine.service.impl;

import com.gihan.AIAuditEngine.entity.Organization;
import com.gihan.AIAuditEngine.repository.OrganizationRepo;
import com.gihan.AIAuditEngine.service.OrganizationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class OrganizationServiceImpl implements OrganizationService {

    private final OrganizationRepo organizationRepo;

    @Autowired
    public OrganizationServiceImpl(OrganizationRepo organizationRepo) {
        this.organizationRepo = organizationRepo;
    }

    @Override
    public List<Organization> getAllOrganizations() {
        return organizationRepo.findAll();
    }

    @Override
    public Organization getOrganizationById(UUID id) {
        return organizationRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Organization not found with id: " + id));
    }
}
