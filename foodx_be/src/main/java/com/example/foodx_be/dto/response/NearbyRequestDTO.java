package com.example.foodx_be.dto.response;

import com.example.foodx_be.dto.request.Request;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NearbyRequestDTO {
    private Request request;
    private LocationRequest locationRequest;
}
