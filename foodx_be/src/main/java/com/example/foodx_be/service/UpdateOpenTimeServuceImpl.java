package com.example.foodx_be.service;

import com.example.foodx_be.dto.OpenTimeDTO;
import com.example.foodx_be.enity.UpdateOpenTime;
import com.example.foodx_be.enity.UpdateRestaurant;
import com.example.foodx_be.repository.UpdateOpentimeRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class UpdateOpenTimeServuceImpl implements UpdateOpenTimeService {
    private UpdateOpentimeRepository updateOpentimeRepository;

    @Override
    public void saveUpdateOpenTime(List<OpenTimeDTO> openTimeDTOList, UpdateRestaurant updateRestaurant) {
        for (OpenTimeDTO openTimeDTO : openTimeDTOList) {
            UpdateOpenTime updateOpenTime = convertToUpdateOpenTimeEnity(openTimeDTO);
            updateOpenTime.setUpdateRestaurant(updateRestaurant);
            updateOpentimeRepository.save(updateOpenTime);
        }
    }

    public UpdateOpenTime convertToUpdateOpenTimeEnity (OpenTimeDTO openTimeDTO){
        return UpdateOpenTime.builder()
                .dayOfWeek(openTimeDTO.getDayOfWeek())
                .openingTime(openTimeDTO.getOpeningTime())
                .closingTime(openTimeDTO.getClosingTime())
                .build();
    }
}
