package com.obs.repo;

import com.obs.entity.User;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepo extends JpaRepository<User, Long> {
    // Default inherit the read-only transaction
    Optional<User> findByUsername(String username);

    /**
     * Similar to findByUsername but eager load the roles attribute.
     *
     * @param username
     * @return
     */
    @EntityGraph(attributePaths = {"roles"})
    Optional<User> readByUsername(String username);
}
