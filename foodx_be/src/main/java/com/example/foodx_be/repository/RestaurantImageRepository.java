package com.example.foodx_be.repository;

import com.example.foodx_be.enity.RestaurantImage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface RestaurantImageRepository extends JpaRepository<RestaurantImage, UUID> {
}
