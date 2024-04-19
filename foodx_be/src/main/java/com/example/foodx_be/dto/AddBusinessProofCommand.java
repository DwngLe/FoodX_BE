package com.example.foodx_be.dto;

import com.example.foodx_be.ulti.OwnerRole;
import com.example.foodx_be.ulti.UpdateState;
import lombok.Builder;
import lombok.Data;

import java.util.UUID;
@Data
@Builder
public class AddBusinessProofCommand {
    private OwnerRole ownerRole;
    private UUID idUser;
    private UUID idRestaurant;
}
