package com.example.foodx_be.mapper;

import com.example.foodx_be.dto.request.RestaurantUpdateRequest;
import com.example.foodx_be.enity.UpdateRestaurant;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UpdateRestaurantMapper {
    UpdateRestaurant toUpdateRestaurant(RestaurantUpdateRequest restaurantUpdateRequest);
}
