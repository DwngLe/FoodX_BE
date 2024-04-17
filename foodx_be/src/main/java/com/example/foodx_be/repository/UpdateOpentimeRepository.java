package com.example.foodx_be.repository;

import com.example.foodx_be.enity.UpdateOpenTime;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface UpdateOpentimeRepository extends JpaRepository<UpdateOpenTime, UUID> {
}
