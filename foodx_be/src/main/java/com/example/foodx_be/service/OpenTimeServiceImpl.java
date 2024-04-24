package com.example.foodx_be.service;

import com.example.foodx_be.dto.OpenTimeDTO;
import com.example.foodx_be.enity.OpenTime;
import com.example.foodx_be.enity.Restaurant;
import com.example.foodx_be.repository.OpenTimeRepository;
import com.example.foodx_be.repository.RestaurantRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
@Service
@AllArgsConstructor
public class OpenTimeServiceImpl implements OpenTimeService {
    private OpenTimeRepository openTimeRepository;

    @Override
    public void saveOpenTime(List<OpenTimeDTO> openTimeDTOList, Restaurant restaurant) {
        List<OpenTime> openTimeList = convertToOpenTimeEnityList(openTimeDTOList);
        for (OpenTime openTime : openTimeList) {
            openTime.setRestaurant(restaurant);
            openTimeRepository.save(openTime);
        }
    }

    @Override
    public List<OpenTimeDTO> getOpenTimeOfRestaurant(UUID idRestaurant) {
        List<OpenTime> openTimeList = openTimeRepository.findAllByRestaurantId(idRestaurant);
        List<OpenTimeDTO> openTimeDTOList = convertToOpenTimeDTOList(openTimeList);
        return openTimeDTOList;
    }

    @Override
    public OpenTime convertToOpenTimeEnity(OpenTimeDTO openTimeDTO) {
        return OpenTime.builder()
                .dayOfWeek(openTimeDTO.getDayOfWeek())
                .openingTime(openTimeDTO.getOpeningTime())
                .closingTime(openTimeDTO.getClosingTime())
                .build();
    }

    @Override
    public List<OpenTime> convertToOpenTimeEnityList(List<OpenTimeDTO> openTimeDTOList) {
        List<OpenTime> openTimeList = new ArrayList<>();
        for(OpenTimeDTO openTimeDTO: openTimeDTOList){
            openTimeList.add(convertToOpenTimeEnity(openTimeDTO));
        }
        return openTimeList;
    }

    @Override
    public List<OpenTimeDTO> convertToOpenTimeDTOList(List<OpenTime> openTimeList) {
        List<OpenTimeDTO> openTimeDTOList = new ArrayList<>();
        for (OpenTime openTime : openTimeList) {
            openTimeDTOList.add(convertToOpenTimeDTO(openTime));
        }
        return openTimeDTOList;
    }

    @Override
    public OpenTimeDTO convertToOpenTimeDTO(OpenTime openTime) {
        return OpenTimeDTO.builder()
                .openingTime(openTime.getOpeningTime())
                .closingTime(openTime.getClosingTime())
                .dayOfWeek(openTime.getDayOfWeek())
                .build();
    }


}

