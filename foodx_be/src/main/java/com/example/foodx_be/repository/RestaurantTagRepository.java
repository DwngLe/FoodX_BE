package com.example.foodx_be.repository;

import com.example.foodx_be.enity.RestaurantTag;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface RestaurantTagRepository extends JpaRepository<RestaurantTag, UUID> {
    List<RestaurantTag> getAllByTagId(UUID id);

    List<RestaurantTag> getAllByRestaurantId(UUID idRestaurant);
}
