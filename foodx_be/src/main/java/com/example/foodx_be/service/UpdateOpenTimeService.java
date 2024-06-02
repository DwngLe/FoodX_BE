package com.example.foodx_be.service;

import com.example.foodx_be.dto.response.OpenTimeDTO;
import com.example.foodx_be.enity.UpdateRestaurant;

import java.util.List;

public interface UpdateOpenTimeService {
    void saveUpdateOpenTime(List<OpenTimeDTO> openTimeDTOList, UpdateRestaurant updateRestaurant);
}
