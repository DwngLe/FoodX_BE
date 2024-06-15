package com.example.foodx_be.service;

import com.example.foodx_be.dto.request.Request;
import com.example.foodx_be.enity.Restaurant;
import com.example.foodx_be.enity.RestaurantTag;
import com.example.foodx_be.enity.Tag;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.UUID;

public interface RestaurantTagService {
    List<Tag> getListTagOfRestaurant(UUID idRestaurant);

    Page<RestaurantTag> getListRestaurantByTag(Request request);

    void saveRestaurantTag(Restaurant restaurant, Tag tag);
}
