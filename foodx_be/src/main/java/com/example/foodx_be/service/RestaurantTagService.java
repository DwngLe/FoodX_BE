package com.example.foodx_be.service;

import com.example.foodx_be.enity.Tag;

import java.util.List;
import java.util.UUID;

public interface RestaurantTagService {
    List<Tag> getListTagOfRestaurant(UUID idRestaurant);
}