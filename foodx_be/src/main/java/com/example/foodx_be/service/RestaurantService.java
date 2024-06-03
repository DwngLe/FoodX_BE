package com.example.foodx_be.service;

import com.example.foodx_be.dto.request.RestaurantCreationRequest;
import com.example.foodx_be.dto.request.RestaurantUpdateRequest;
import com.example.foodx_be.dto.response.LocationDTO;
import com.example.foodx_be.dto.response.RequestDTO;
import com.example.foodx_be.dto.response.RestaurantDTO;
import com.example.foodx_be.enity.Restaurant;
import com.example.foodx_be.enity.RestaurantTag;
import com.example.foodx_be.ulti.RestaurantState;
import org.springframework.data.domain.Page;

import java.util.UUID;

public interface RestaurantService {
    void addRestaurant(RestaurantCreationRequest restaurantCreationRequest);

    RestaurantDTO getRestaurantDTO(UUID idRestaurant);

    Page<RestaurantDTO> getRestaurantByRestaurantState(int pageNo, int limit, RestaurantState restaurantState);

    Page<RestaurantDTO> getRestaurantBySpecification(RequestDTO requestDTO);

    Restaurant getRestaurantEnity(UUID idRestaurant);

    Page<RestaurantTag> getListRestaurantByTag(RequestDTO requestDTO);

    Page<RestaurantDTO> getNearByRestaurant(RequestDTO requestDTO, LocationDTO locationDTO);

    void updateRestaurant(UUID restaurant, RestaurantUpdateRequest restaurantUpdateRequest);

    void saveRestaurantEnity(Restaurant restaurant);

    Restaurant updateRestaurantPoint(UUID idRestaurant, double point, double count);



}
