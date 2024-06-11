package com.example.foodx_be.service;

import java.util.List;

import com.example.foodx_be.dto.response.OpenTimeDTO;
import com.example.foodx_be.enity.UpdateRestaurant;

public interface UpdateOpenTimeService {
    void saveUpdateOpenTime(List<OpenTimeDTO> openTimeDTOList, UpdateRestaurant updateRestaurant);
}
