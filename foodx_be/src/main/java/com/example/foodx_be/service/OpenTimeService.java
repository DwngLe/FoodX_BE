package com.example.foodx_be.service;

import com.example.foodx_be.dto.response.OpenTimeDTO;
import com.example.foodx_be.enity.OpenTime;
import com.example.foodx_be.enity.Restaurant;

import java.util.List;
import java.util.UUID;

public interface OpenTimeService {
    List<OpenTimeDTO> getOpenTimeOfRestaurant(UUID idRestaurant);

    List<OpenTimeDTO> convertToOpenTimeDTOList(List<OpenTime> openTimeList);

    OpenTimeDTO convertToOpenTimeDTO(OpenTime openTime);

    OpenTime convertToOpenTimeEnity(OpenTimeDTO openTimeDTO);

    List<OpenTime> convertToOpenTimeEnityList(List<OpenTimeDTO> openTimeDTOList);

    void saveOpenTime(List<OpenTimeDTO> openTimeDTOList, Restaurant restaurant);
}
