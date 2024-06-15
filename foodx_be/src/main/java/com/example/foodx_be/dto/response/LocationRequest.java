package com.example.foodx_be.dto.response;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class LocationRequest {
    private BigDecimal latitude;
    private BigDecimal longitude;
    private BigDecimal radius;
}
