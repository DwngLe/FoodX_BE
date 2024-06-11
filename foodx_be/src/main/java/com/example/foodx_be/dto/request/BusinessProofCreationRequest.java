package com.example.foodx_be.dto.request;

import com.example.foodx_be.enums.OwnerRole;
import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class BusinessProofCreationRequest {
    private OwnerRole ownerRole;
    private UUID idRestaurant;
}
