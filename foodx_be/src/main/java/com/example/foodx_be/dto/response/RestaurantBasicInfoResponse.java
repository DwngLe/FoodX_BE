package com.example.foodx_be.dto.response;

import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
@Builder
public class RestaurantBasicInfoResponse {
    private UUID id;
    private String restaurantName;
    private String description;
    private Boolean offerDelivery;
    private Boolean offerTakeaway;
    private Boolean outdoorSeating;
    private double points;
    private List<TagDTO> tagDTOList;
    private List<RestaurantImageResponse> restaurantImageResponseList;

}
