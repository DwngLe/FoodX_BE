package com.example.foodx_be.dto.response;

import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class TagDTO {
    private UUID id;
    private String tagName;
    private String tagDescription;
}
