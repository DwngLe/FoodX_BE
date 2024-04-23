package com.example.foodx_be.dto;

import com.example.foodx_be.validation.PasswordMatches;
import com.example.foodx_be.validation.ValidName;
import com.example.foodx_be.validation.ValidUsername;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@PasswordMatches
public class RegisterCommand {
    @ValidUsername
    @NotBlank
    private String username;
    @NotBlank(message = "Password cannot be empty")
    private String password;
    @NotBlank(message = "Repeat Password cannot be empty")
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
