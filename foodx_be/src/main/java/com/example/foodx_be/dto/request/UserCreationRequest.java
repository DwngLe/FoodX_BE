package com.example.foodx_be.dto.request;

import com.example.foodx_be.validation.ValidName;
import com.example.foodx_be.validation.ValidUsername;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UserCreationRequest {
    @ValidUsername(message = "INVALID_USERNAME")
    @NotBlank
    @Size(min = 5, message = "INVALID_USERNAME")
    private String username;
    @NotBlank(message = "Password cannot be empty")
    @Size(min = 8, message = "INVALID_PASSWORD")
    private String password;
    @NotBlank(message = "REPEAT_PASSWORD_CANNOT_BE_EMPTY")
    private String repeatPassword;
    @ValidName
    @NotBlank(message = "Name cannot be empty")
    private String name;
    @NotBlank(message = "Phone cannot be empty")
    private String phoneNumber;
    @Email(message = "Email is not valid", regexp = "^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$")
    @NotBlank(message = "Email cannot be empty")
    private String email;
    @NotBlank(message = "Ward cannot be empty")
    private String ward;
    @NotBlank(message = "Ward cannot be empty")
    private String district;
    @NotBlank(message = "Ward cannot be empty")
    private String city;
}
