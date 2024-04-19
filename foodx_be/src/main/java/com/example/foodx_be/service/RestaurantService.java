package com.example.foodx_be.service;

import com.example.foodx_be.dto.AddRestaurantCommand;
import com.example.foodx_be.dto.RestaurantDTO;
import com.example.foodx_be.dto.RestaurantStateCommand;
import com.example.foodx_be.dto.UpdateRestaurantCommand;
import com.example.foodx_be.enity.Restaurant;
import com.example.foodx_be.ulti.RestaurantState;
import org.springframework.data.domain.Page;

import java.math.BigDecimal;
import java.util.UUID;

public interface RestaurantService {
    void addRestaurant(AddRestaurantCommand addRestaurantCommand);

    RestaurantDTO getRestaurantDTO(UUID idRestaurant);

    Page<RestaurantDTO> getRestaurantsByKeyword(int page, int limit, String keyword, String searchType);

    Page<RestaurantDTO> getRestaurantByRestaurantState(int pageNo, int limit, RestaurantState restaurantState);

    Restaurant getRestaurantEnity(UUID idRestaurant);

    Restaurant getRestaurantEnityByName(String restaurantName);

    Page<RestaurantDTO> getNearByRestaurant(BigDecimal longitude, BigDecimal latitude, double radiusInKm, int pageNo, int limit);

    void updateRestaurant(UUID restaurant, UpdateRestaurantCommand updateRestaurantCommand);

    void saveRestaurantEnity(Restaurant restaurant);



}
