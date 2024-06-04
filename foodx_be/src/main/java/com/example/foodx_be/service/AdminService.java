package com.example.foodx_be.service;

import com.example.foodx_be.dto.response.RestaurantUpdateDTO;
import com.example.foodx_be.ulti.RestaurantState;
import com.example.foodx_be.ulti.UpdateState;
import org.springframework.data.domain.Page;

import java.util.UUID;

public interface AdminService {
    Page<RestaurantUpdateDTO> getRestaurantUpdateList(int pageNo, int limit, UpdateState updateState);
    void reviewRestaurantUpdate(UUID idRestaurantUpdate, UpdateState updateState);
    void reviewRestaurantState(UUID idRestaurant, RestaurantState restaurantState);

}
