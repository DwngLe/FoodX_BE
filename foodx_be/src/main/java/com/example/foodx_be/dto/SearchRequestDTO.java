package com.example.foodx_be.dto;

import com.example.foodx_be.ulti.Operation;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SearchRequestDTO {
    String column;
    String value;
    private Operation operation;
}