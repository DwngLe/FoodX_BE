package com.example.foodx_be.mapper;

import com.example.foodx_be.dto.request.RestaurantCreationRequest;
import com.example.foodx_be.dto.response.RestaurantResponse;
import com.example.foodx_be.enity.Restaurant;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface RestaurantMapper {
    @Mapping(source = "restaurantTagList", target = "tagDTOList")
    @Mapping(source = "restaurantImageList", target = "restaurantImageResponseList")
    RestaurantResponse toRestaurantResponse(Restaurant restaurant);

    Restaurant toRestaurant(RestaurantCreationRequest restaurantCreationRequest);
}
