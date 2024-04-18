package com.example.foodx_be.service;

import com.example.foodx_be.dto.*;
import com.example.foodx_be.ulti.UpdateState;
import org.springframework.data.domain.Page;

import java.util.UUID;

public interface AdminService {
    Page<RestaurantUpdateDTO> getRestaurantUpdateList(int pageNo, int limit, UpdateState updateState);
    Page<RestaurantDTO> getRestaurantByRestaurantState(int pageNo, int limit, RestaurantStateCommand restaurantStateCommand);
    void reviewRestaurantUpdate(UUID idRestaurantUpdate, ReviewUpdate reviewUpdate);
    void reviewRestaurantState(UUID idRestaurant, ReviewRestaurantState restaurantState);
}
