package com.example.foodx_be.dto;

import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class AddReviewRestaurantCommand {
    private String reviewTitle;
    private String reviewContent;
    private Double starNumber;
    private String username;
    private UUID restaurantId;
}
