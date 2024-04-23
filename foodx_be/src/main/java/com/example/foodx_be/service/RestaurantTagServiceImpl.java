package com.example.foodx_be.service;

import com.example.foodx_be.enity.RestaurantTag;
import com.example.foodx_be.enity.Tag;
import com.example.foodx_be.repository.RestaurantTagRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
public class RestaurantTagServiceImpl implements RestaurantTagService {
    private RestaurantTagRepository restaurantTagRepository;
    @Override
    public List<Tag> getListTagOfRestaurant(UUID idRestaurant) {
        List<Tag> tagList = new ArrayList<>();
        List<RestaurantTag> restaurantTagList = restaurantTagRepository.getAllByRestaurantId(idRestaurant);
        for(RestaurantTag restaurantTag : restaurantTagList){
            tagList.add(restaurantTag.getTag());
        }
        return tagList;
    }
}