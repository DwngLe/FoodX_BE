package com.example.foodx_be.service;

import com.example.foodx_be.dto.AddRestaurantCommand;
import com.example.foodx_be.dto.RestaurantDTO;
import com.example.foodx_be.dto.UpdateRestaurantCommand;
import com.example.foodx_be.enity.Restaurant;
import org.springframework.data.domain.Page;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public interface RestaurantService {
    void addRestaurant(AddRestaurantCommand addRestaurantCommand);

    RestaurantDTO getRestaurantDTO(UUID idRestaurant);

    Page<RestaurantDTO> getRestaurantsByKeyword(int page, int limit, String keyword, String searchType);

    Restaurant getRestaurantEnity(UUID idRestaurant);

    Restaurant getRestaurantEnityByName(String restaurantName);

    List<RestaurantDTO> getNearByRestaurant(BigDecimal longitude, BigDecimal latitude, double radiusInKm);

    void updateRestaurant(UUID restaurant, UpdateRestaurantCommand updateRestaurantCommand);



}
