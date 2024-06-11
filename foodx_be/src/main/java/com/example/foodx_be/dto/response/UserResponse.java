package com.example.foodx_be.dto.response;

import java.time.LocalDate;
import java.util.Set;
import java.util.UUID;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class UserResponse {
    private UUID id;
    private String name;
    private String phoneNumber;
    private String email;
    private String avatarLink;
    private String facebookLink;
    private String instagramLink;
    private String ward;
    private String district;
    private String city;
    private LocalDate jointDate;
    private int points;
    private Set<String> roles;
}
