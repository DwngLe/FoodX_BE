package com.example.foodx_be.repository;

import com.example.foodx_be.enity.Restaurant;
import com.example.foodx_be.enums.RestaurantState;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;
import java.util.UUID;

public interface RestaurantRepository extends JpaRepository<Restaurant, UUID>, JpaSpecificationExecutor<Restaurant> {
    List<Restaurant> findAllByRestaurantState(RestaurantState restaurantState);
}
