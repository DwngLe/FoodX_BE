package com.example.foodx_be.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class OpenTimeDTO {
    private String dayOfWeek;
    @JsonFormat(pattern = "HH:mm:ss")
    private String openingTime;
    @JsonFormat(pattern = "HH:mm:ss")
    private String closingTime;
}
