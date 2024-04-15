package com.example.foodx_be.dto;

import com.example.foodx_be.enity.ReviewImage;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
@Builder
public class ReviewRestaurantDTO {
    private UUID id;
    private LocalDateTime reviewDate;
    private String reviewTitle;
    private String reviewContent;
    private Double starNumber;
    private int likeNumber;
    private int unlikeNumber;
    private UserDTO userId;
    private UUID restaurantId;
    private List<ReviewImage> reviewImageIds;
}
