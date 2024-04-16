package com.example.foodx_be.service;

import com.example.foodx_be.enity.ReviewImage;
import com.example.foodx_be.repository.ReviewImageRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
@Service
@AllArgsConstructor
public class RestaurantReviewImageImpl implements RestaurantReviewImageService{
    ReviewImageRepository reviewImageRepository;
    ReviewService reviewService;
    @Override
    public List<ReviewImage> reviewImageList() {
        return reviewImageRepository.findAll();
    }

    @Override
    public void save(UUID idReview ,ReviewImage reviewImage) {
        reviewImage.setReview(reviewService.getReview(idReview));
        reviewImageRepository.save(reviewImage);
    }

    @Override
    public void delete(UUID idImage) {

    }
}
