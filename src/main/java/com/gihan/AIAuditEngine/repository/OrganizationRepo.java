package com.gihan.AIAuditEngine.repository;

import com.gihan.AIAuditEngine.entity.Organization;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface OrganizationRepo extends JpaRepository<Organization, UUID> {
    Optional<Organization> findBySlug(String slug);
    boolean existsBySlug(String slug);
    boolean existsByName(String name);
}
