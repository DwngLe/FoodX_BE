package com.example.foodx_be.ulti;

import lombok.experimental.UtilityClass;

import java.util.UUID;

@UtilityClass
public class UUIDUtil {
    private String generateUUID() {
        return UUID.randomUUID().toString();
    }

    private boolean validateId(String id) {
        try {
            UUID.fromString(id);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }
}
