package com.gihan.AIAuditEngine.repository;

import com.gihan.AIAuditEngine.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface UserRepo extends JpaRepository<User, UUID> {
    boolean existsByEmail(String email);
}
