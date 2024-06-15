package com.example.foodx_be.service;

import com.example.foodx_be.dto.request.Request;
import com.example.foodx_be.dto.request.ReviewRestaurantCreationRequest;
import com.example.foodx_be.dto.response.ReviewRestaurantDTO;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface ReviewService {
    void addReview(ReviewRestaurantCreationRequest reviewCommand, MultipartFile[] multipartFiles) throws IOException;

    Page<ReviewRestaurantDTO> getListReviewBySpecification(Request request);

    Page<ReviewRestaurantDTO> getMyReviewSpecification(Request request);

    List<ReviewRestaurantDTO> getListRecentReview(int pageNo, int limit);
}
