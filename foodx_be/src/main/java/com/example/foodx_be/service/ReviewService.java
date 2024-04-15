package com.example.foodx_be.service;

import com.example.foodx_be.dto.AddReviewRestaurantCommand;
import com.example.foodx_be.dto.ReviewRestaurantDTO;
import com.example.foodx_be.enity.Review;
import org.springframework.data.domain.Page;

import java.util.UUID;

public interface ReviewService {
    void addReview(AddReviewRestaurantCommand reviewCommand);

    Page<ReviewRestaurantDTO> getListReviewOfRestaurant(int pageNo, int limit, UUID idRestaurant);
}
