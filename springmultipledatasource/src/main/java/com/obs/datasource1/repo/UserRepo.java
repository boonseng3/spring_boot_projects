package com.obs.datasource1.repo;

import com.obs.datasource1.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepo extends JpaRepository<User, Long> {
    // Default inherit the read-only transaction
    Optional<User> findByUsername(String username);
}
