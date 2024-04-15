package com.example.foodx_be.dto;

import com.example.foodx_be.ulti.UpdateState;
import lombok.*;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ReviewUpdate {
    UpdateState updateState;
}
