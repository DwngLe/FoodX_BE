package com.example.foodx_be.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class LocationDTO {
    private BigDecimal latitude;
    private BigDecimal longitude;
    private BigDecimal radius;
}
