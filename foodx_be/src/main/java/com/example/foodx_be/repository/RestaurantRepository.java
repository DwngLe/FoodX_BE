package com.example.foodx_be.repository;

import com.example.foodx_be.enity.Restaurant;
import com.example.foodx_be.ulti.RestaurantState;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface RestaurantRepository extends JpaRepository<Restaurant, UUID> {
    Optional<Restaurant> findRestaurantByRestaurantName(String restaurantName);
    List<Restaurant> findAllByRestaurantNameAndRestaurantState(String retaurantName,  RestaurantState restaurantState);
    List<Restaurant> findAllByCityAndRestaurantState(String city, RestaurantState restaurantState);
    List<Restaurant> findAllByDistrict(String district);
}
