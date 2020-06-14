package com.pochec.watercounter.repository;

import com.pochec.watercounter.model.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<UserRole, Long> {
    UserRole findByRole(String role);
}
