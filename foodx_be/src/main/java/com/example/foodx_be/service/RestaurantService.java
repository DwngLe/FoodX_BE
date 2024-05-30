package com.example.foodx_be.service;

import com.example.foodx_be.dto.*;
import com.example.foodx_be.enity.Restaurant;
import com.example.foodx_be.enity.RestaurantTag;
import com.example.foodx_be.ulti.RestaurantState;
import org.springframework.data.domain.Page;

import java.util.UUID;

public interface RestaurantService {
    void addRestaurant(AddRestaurantCommand addRestaurantCommand);

    RestaurantDTO getRestaurantDTO(UUID idRestaurant);

    Page<RestaurantDTO> getRestaurantByRestaurantState(int pageNo, int limit, RestaurantState restaurantState);

    Page<RestaurantDTO> getRestaurantBySpecification(RequestDTO requestDTO);

    Restaurant getRestaurantEnity(UUID idRestaurant);

    Page<RestaurantTag> getListRestaurantByTag(RequestDTO requestDTO);

    Page<RestaurantDTO> getNearByRestaurant(RequestDTO requestDTO, LocationDTO locationDTO);

    void updateRestaurant(UUID restaurant, UpdateRestaurantCommand updateRestaurantCommand);

    void saveRestaurantEnity(Restaurant restaurant);

    Restaurant updateRestaurantPoint(UUID idRestaurant, double point, double count);



}
