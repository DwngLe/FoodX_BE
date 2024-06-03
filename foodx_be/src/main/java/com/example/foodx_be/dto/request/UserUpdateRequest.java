package com.example.foodx_be.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserUpdateRequest {
    @NotBlank(message = "Name cannot be empty")
    private String name;
    @NotBlank(message =  "Phone cannot be empty")
    private String phoneNumber;
    @Email(message = "Email is not valid", regexp = "^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$")
    @NotBlank(message = "Email cannot be empty")
    private String email;
    @NotBlank(message = "Avatar cannot be empty")
    private String avatarLink;
    private String facebookLink;
    private String instagramLink;
    @NotBlank(message =  "Ward cannot be empty")
    private String ward;
    @NotBlank(message =  "District cannot be empty")
    private String district;
    @NotBlank(message =  "City cannot be empty")
    private String city;
}
