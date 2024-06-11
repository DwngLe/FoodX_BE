package com.example.foodx_be.dto.response;

import com.example.foodx_be.enums.OwnerRole;
import com.example.foodx_be.enums.UpdateState;
import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class BusinessProofDTO {
    private UUID id;
    private OwnerRole ownerRole;
    private String businessProofUrl;
    private UpdateState updateState;
    private UserResponse userOwner;
    private UUID idRestaurant;
    private String restaurantName;
}
