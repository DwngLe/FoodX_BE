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
    private RestaurantService restaurantService;
    private RestaurantRepository restaurantRepository;
    private OpenTimeRepository openTimeRepository;

    @Override
    public void addOpenTimeToRestaurant(List<OpenTime> openTimeList, UUID idRestaurant) {
        Restaurant restaurant = restaurantService.getRestaurantEnity(idRestaurant);
        restaurant.setOpenTimeList(openTimeList);
        for (OpenTime openTime : openTimeList) {
            openTime.setRestaurant(restaurant);
        }
        restaurantRepository.save(restaurant);
    }


    @Override
    public List<OpenTimeDTO> getOpenTimeOfRestaurant(UUID idRestaurant) {
        List<OpenTime> openTimeList = openTimeRepository.findAllByRestaurantId(idRestaurant);
        List<OpenTimeDTO> openTimeDTOList = convertToOpenTimeDTOList(openTimeList);
        return openTimeDTOList;
    }

    private List<OpenTimeDTO> convertToOpenTimeDTOList(List<OpenTime> openTimeList) {
        List<OpenTimeDTO> openTimeDTOList = new ArrayList<>();
        for (OpenTime openTime : openTimeList) {
            openTimeDTOList.add(convertToOpenTimeDTO(openTime));
        }
        return openTimeDTOList;
    }

    public OpenTimeDTO convertToOpenTimeDTO(OpenTime openTime) {
        return OpenTimeDTO.builder()
                .openingTime(openTime.getOpeningTime())
                .closingTime(openTime.getClosingTime())
                .dayOfWeek(openTime.getDayOfWeek())
                .build();
    }
}

