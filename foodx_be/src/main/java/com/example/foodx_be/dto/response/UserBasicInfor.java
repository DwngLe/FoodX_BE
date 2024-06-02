package com.example.foodx_be.dto.response;

import lombok.Builder;
import lombok.Data;

import java.util.Set;
import java.util.UUID;

@Data
@Builder
public class UserBasicInfor {
    private UUID idUser;
    private String name;
    private String avatarLink;
    private int points;
    private Set<String> roles;
}
