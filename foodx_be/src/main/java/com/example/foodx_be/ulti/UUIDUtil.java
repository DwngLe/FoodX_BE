package com.example.foodx_be.ulti;

import lombok.experimental.UtilityClass;

import java.util.UUID;

@UtilityClass
public class UUIDUtil {
    private String generateUUID() {
        return UUID.randomUUID().toString();
    }

}
