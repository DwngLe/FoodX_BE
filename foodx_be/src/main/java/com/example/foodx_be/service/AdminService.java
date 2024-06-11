package com.example.foodx_be.service;

import java.util.UUID;

import org.springframework.data.domain.Page;

import com.example.foodx_be.dto.response.RestaurantUpdateDTO;
import com.example.foodx_be.enums.RestaurantState;
import com.example.foodx_be.enums.UpdateState;

public interface AdminService {
    Page<RestaurantUpdateDTO> getRestaurantUpdateList(int pageNo, int limit, UpdateState updateState);

    void reviewRestaurantUpdate(UUID idRestaurantUpdate, UpdateState updateState);

    void reviewRestaurantState(UUID idRestaurant, RestaurantState restaurantState);
}
