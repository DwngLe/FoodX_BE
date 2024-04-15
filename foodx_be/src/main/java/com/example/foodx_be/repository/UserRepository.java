package com.example.foodx_be.repository;

import com.example.foodx_be.enity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.List;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {
    Optional<User> findByUsername(String username);
    Optional<User> findById(UUID id);
    List<User> findByNameContaining(String name);
}
