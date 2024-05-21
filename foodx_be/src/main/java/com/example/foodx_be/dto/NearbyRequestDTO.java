package com.example.foodx_be.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NearbyRequestDTO {
    private RequestDTO requestDTO;
    private LocationDTO locationDTO;
}
