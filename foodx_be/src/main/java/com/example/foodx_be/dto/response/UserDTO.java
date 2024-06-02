package com.example.foodx_be.dto.response;

import lombok.*;

import java.time.LocalDate;
import java.util.Set;
import java.util.UUID;
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class UserDTO {
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
