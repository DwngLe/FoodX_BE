package com.example.foodx_be.dto;

import com.example.foodx_be.ulti.Operation;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SearchRequestDTO {
    String column;
    String value;
    private Operation operation;
}
