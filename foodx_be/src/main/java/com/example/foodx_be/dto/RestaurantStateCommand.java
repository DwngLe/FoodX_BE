package com.example.foodx_be.dto;

import com.example.foodx_be.ulti.RestaurantState;
import lombok.*;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class RestaurantStateCommand {
    RestaurantState restaurantState;
}
