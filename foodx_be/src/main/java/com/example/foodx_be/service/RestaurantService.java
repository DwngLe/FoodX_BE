package com.example.foodx_be.service;

import com.example.foodx_be.dto.AddRestaurantCommand;
import com.example.foodx_be.dto.RequestDTO;
import com.example.foodx_be.dto.RestaurantDTO;
import com.example.foodx_be.dto.UpdateRestaurantCommand;
import com.example.foodx_be.ulti.RestaurantState;
import org.springframework.data.domain.Page;

import java.math.BigDecimal;
import java.util.UUID;

public interface RestaurantService {
    void addRestaurant(AddRestaurantCommand addRestaurantCommand);

    RestaurantDTO getRestaurantDTO(UUID idRestaurant);

    Page<RestaurantDTO> getRestaurantsByKeyword(int page, int limit, String keyword, String searchType);

    Page<RestaurantDTO> getRestaurantByRestaurantState(int pageNo, int limit, RestaurantState restaurantState);

    Page<RestaurantDTO> getRestaurantBySpecification(RequestDTO requestDTO);


    com.example.foodx_be.enity.Restaurant getRestaurantEnity(UUID idRestaurant);

    com.example.foodx_be.enity.Restaurant getRestaurantEnityByName(String restaurantName);
    Page<RestaurantDTO> getListRestaurantByTag(int pageNo, int limit, UUID idTag);

    Page<RestaurantDTO> getNearByRestaurant(BigDecimal longitude, BigDecimal latitude, double radiusInKm, int pageNo, int limit);

    void updateRestaurant(UUID restaurant, UpdateRestaurantCommand updateRestaurantCommand);

    void saveRestaurantEnity(com.example.foodx_be.enity.Restaurant restaurant);

    com.example.foodx_be.enity.Restaurant updateRestaurantPoint(UUID idRestaurant, double point, double count);



}
