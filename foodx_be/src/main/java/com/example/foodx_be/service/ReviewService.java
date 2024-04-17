package com.example.foodx_be.service;

import com.example.foodx_be.dto.AddReviewRestaurantCommand;
import com.example.foodx_be.dto.ReviewRestaurantDTO;
import com.example.foodx_be.enity.Review;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

public interface ReviewService {
    void addReview(AddReviewRestaurantCommand reviewCommand, MultipartFile[] multipartFiles) throws IOException;

    Review getReview(UUID id);

    Page<ReviewRestaurantDTO> getListReviewOfRestaurant(int pageNo, int limit, UUID idRestaurant);
    List<ReviewRestaurantDTO> getListRecentReview(int pageNo, int limit);
}
