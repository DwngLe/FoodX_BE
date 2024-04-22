package com.example.foodx_be.repository;

import com.example.foodx_be.enity.Tag;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface TagRepository  extends JpaRepository<Tag, UUID> {
}
