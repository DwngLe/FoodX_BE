package com.example.foodx_be.repository;

import com.example.foodx_be.enity.OpenTime;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface OpenTimeRepository extends JpaRepository<OpenTime, UUID> {
    List<OpenTime> findAllByRestaurantId(UUID idRestaurant);
}
