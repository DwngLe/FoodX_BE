package com.example.foodx_be.service;

import com.example.foodx_be.dto.request.Request;
import com.example.foodx_be.dto.request.RestaurantCreationRequest;
import com.example.foodx_be.dto.request.RestaurantUpdateRequest;
import com.example.foodx_be.dto.response.LocationRequest;
import com.example.foodx_be.dto.response.RestaurantBasicInfoResponse;
import com.example.foodx_be.dto.response.RestaurantResponse;
import com.example.foodx_be.enity.Restaurant;
import com.example.foodx_be.enity.RestaurantTag;
import com.example.foodx_be.enums.RestaurantState;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

public interface RestaurantService {
    void addRestaurant(RestaurantCreationRequest restaurantCreationRequest, MultipartFile[] multipartFiles) throws IOException;

    RestaurantResponse getRestaurantDTO(UUID idRestaurant);

    Page<RestaurantBasicInfoResponse> getRestaurantByRestaurantState(int pageNo, int limit, RestaurantState restaurantState);

    Page<RestaurantBasicInfoResponse> getRestaurantBySpecification(Request request);

    Restaurant getRestaurantEnity(UUID idRestaurant);

    Page<RestaurantTag> getListRestaurantByTag(Request request);

    Page<RestaurantBasicInfoResponse> getNearByRestaurant(Request request, LocationRequest locationRequest);

    void updateRestaurant(UUID restaurant, RestaurantUpdateRequest restaurantUpdateRequest);

    void saveRestaurantEnity(Restaurant restaurant);

    Restaurant updateRestaurantPoint(UUID idRestaurant, double point, double count);
}
