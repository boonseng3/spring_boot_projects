package com.obs.datasource2.repo;

import com.obs.datasource2.entity.AlternateUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AlternateUserRepo extends JpaRepository<AlternateUser, Long> {
    // Default inherit the read-only transaction
    Optional<AlternateUser> findByUsername(String username);
}
