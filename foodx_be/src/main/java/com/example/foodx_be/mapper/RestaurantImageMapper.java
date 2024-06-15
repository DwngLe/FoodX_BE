package com.example.foodx_be.mapper;

import com.example.foodx_be.dto.response.RestaurantImageResponse;
import com.example.foodx_be.enity.RestaurantImage;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface RestaurantImageMapper {
    RestaurantImageResponse toRestaurantImageResponse(RestaurantImage restaurantImage);
}
