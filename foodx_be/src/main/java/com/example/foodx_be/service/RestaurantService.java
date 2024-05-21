package com.example.foodx_be.service;

import com.example.foodx_be.dto.*;
import com.example.foodx_be.ulti.RestaurantState;
import org.springframework.data.domain.Page;

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

    Page<RestaurantDTO> getNearByRestaurant(RequestDTO requestDTO, LocationDTO locationDTO);

    void updateRestaurant(UUID restaurant, UpdateRestaurantCommand updateRestaurantCommand);

    void saveRestaurantEnity(com.example.foodx_be.enity.Restaurant restaurant);

    com.example.foodx_be.enity.Restaurant updateRestaurantPoint(UUID idRestaurant, double point, double count);



}
