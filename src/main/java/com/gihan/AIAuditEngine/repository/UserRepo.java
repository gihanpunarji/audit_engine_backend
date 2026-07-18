package com.gihan.AIAuditEngine.repository;

import com.gihan.AIAuditEngine.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface UserRepo extends JpaRepository<User, UUID> {
    boolean existsByEmail(String email);

    User findByEmail(String email);
}
