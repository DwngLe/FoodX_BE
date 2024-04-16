package com.example.foodx_be.service;

import com.example.foodx_be.enity.ReviewImage;

import java.util.List;
import java.util.UUID;

public interface RestaurantReviewImageService {
    List<ReviewImage> reviewImageList();
    void save(UUID idReview, ReviewImage  reviewImage);
    void delete(UUID idImage);

}
