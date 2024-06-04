package com.example.foodx_be.dto.request;

import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class ReviewRestaurantCreationRequest {
    private String reviewTitle;
    private String reviewContent;
    private Double starNumber;
    private UUID restaurantId;
}
