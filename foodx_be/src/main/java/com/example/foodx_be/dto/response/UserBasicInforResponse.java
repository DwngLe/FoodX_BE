package com.example.foodx_be.dto.response;

import java.util.Set;
import java.util.UUID;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserBasicInforResponse {
    private UUID idUser;
    private String name;
    private String avatarLink;
    private int points;
    private Set<String> roles;
}
