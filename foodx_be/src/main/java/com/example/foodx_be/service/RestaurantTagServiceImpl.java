package com.example.foodx_be.service;

import com.example.foodx_be.dto.response.PageRequestDTO;
import com.example.foodx_be.dto.response.RequestDTO;
import com.example.foodx_be.enity.Restaurant;
import com.example.foodx_be.enity.RestaurantTag;
import com.example.foodx_be.enity.Tag;
import com.example.foodx_be.exception.NoResultsFoundException;
import com.example.foodx_be.repository.RestaurantTagRepository;
import com.example.foodx_be.ulti.GlobalOperator;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
public class RestaurantTagServiceImpl implements RestaurantTagService {
    private FiltersSpecificationImpl<RestaurantTag> tagFiltersSpecification;

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

    @Override
    public Page<RestaurantTag> getListRestaurantByTag(RequestDTO requestDTO) {
        Specification<RestaurantTag> restaurantSpecification = tagFiltersSpecification.getSearchSpecification(requestDTO.getSearchRequestDTO(), GlobalOperator.OR);
        Pageable pageable = new PageRequestDTO().getPageable(requestDTO.getPageRequestDTO());

        if ("point".equals(requestDTO.getSortByColumn())) {
            restaurantSpecification = restaurantSpecification.and(tagFiltersSpecification.sortByAverageReview(requestDTO.getSort()));
        } else {
            restaurantSpecification = restaurantSpecification.and(tagFiltersSpecification.sortByColumn(requestDTO.getSortByColumn(), requestDTO.getSort()));
        }

        Page<RestaurantTag> all = restaurantTagRepository.findAll(restaurantSpecification, pageable);
        if (all.getContent().isEmpty()) {
            throw new NoResultsFoundException();
        }

        return all;
    }

    @Override
    public void saveRestaurantTag(Restaurant restaurant, Tag tag) {
        RestaurantTag restaurantTag = new RestaurantTag();
        restaurantTag.setRestaurant(restaurant);
        restaurantTag.setTag(tag);
        restaurantTagRepository.save(restaurantTag);
    }
}
