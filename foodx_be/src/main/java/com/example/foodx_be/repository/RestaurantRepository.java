package com.example.foodx_be.repository;

import com.example.foodx_be.enity.Restaurant;
import com.example.foodx_be.enums.RestaurantState;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.UUID;

public interface RestaurantRepository extends JpaRepository<Restaurant, UUID>, JpaSpecificationExecutor<Restaurant> {
    Page<Restaurant> findAllByRestaurantState(RestaurantState restaurantState, Pageable pageable);
}
