package com.example.foodx_be.service;

import com.example.foodx_be.dto.request.Request;
import com.example.foodx_be.dto.response.PageRequestDTO;
import com.example.foodx_be.enity.Restaurant;
import com.example.foodx_be.enity.RestaurantTag;
import com.example.foodx_be.enity.Tag;
import com.example.foodx_be.enums.GlobalOperator;
import com.example.foodx_be.exception.AppException;
import com.example.foodx_be.exception.ErrorCode;
import com.example.foodx_be.repository.RestaurantTagRepository;
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
        for (RestaurantTag restaurantTag : restaurantTagList) {
            tagList.add(restaurantTag.getTag());
        }
        return tagList;
    }

    @Override
    public Page<RestaurantTag> getListRestaurantByTag(Request request) {
        Specification<RestaurantTag> restaurantSpecification =
                tagFiltersSpecification.getSearchSpecification(request.getSearchRequestDTO(), GlobalOperator.OR);
        Pageable pageable = new PageRequestDTO().getPageable(request.getPageRequestDTO());

        if ("point".equals(request.getSortByColumn())) {
            restaurantSpecification =
                    restaurantSpecification.and(tagFiltersSpecification.sortByAverageReview(request.getSort()));
        } else {
            restaurantSpecification = restaurantSpecification.and(
                    tagFiltersSpecification.sortByColumn(request.getSortByColumn(), request.getSort()));
        }

        Page<RestaurantTag> all = restaurantTagRepository.findAll(restaurantSpecification, pageable);
        if (all.getContent().isEmpty()) {
            throw new AppException(ErrorCode.TAG_NOT_EXISTED);
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
