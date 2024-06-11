package com.example.foodx_be.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.foodx_be.enity.UpdateOpenTime;

public interface UpdateOpentimeRepository extends JpaRepository<UpdateOpenTime, UUID> {
}
