package com.example.foodx_be.service;

import com.example.foodx_be.dto.OpenTimeDTO;
import com.example.foodx_be.enity.OpenTime;

import java.util.List;
import java.util.UUID;

public interface OpenTimeService {
    void addOpenTimeToRestaurant(List<OpenTime> openTimeList, UUID idRestaurant);
    List<OpenTimeDTO> getOpenTimeOfRestaurant(UUID idRestaurant);
}
