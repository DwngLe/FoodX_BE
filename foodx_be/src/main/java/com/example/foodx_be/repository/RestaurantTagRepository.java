package com.example.foodx_be.repository;

import com.example.foodx_be.enity.RestaurantTag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;
import java.util.UUID;

public interface RestaurantTagRepository
        extends JpaRepository<RestaurantTag, UUID>, JpaSpecificationExecutor<RestaurantTag> {

    List<RestaurantTag> getAllByRestaurantId(UUID idRestaurant);
}
