package com.example.foodx_be.dto.request;

import com.example.foodx_be.dto.response.OpenTimeDTO;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Data
@Builder
public class AddRestaurantCommand {
    @NotBlank(message = "Name cannot be empty")
    private String restaurantName;
    @NotBlank(message = "House number cannot be empty")
    private String houseNumber;
    @NotBlank(message = "Ward cannot be empty")
    private String ward;
    @NotBlank(message = "District cannot be empty")
    private String district;
    @NotBlank(message = "City cannot be empty")
    private String city;
    @NotBlank(message = "Longitude location cannot be empty")
    private BigDecimal longitude;
    @NotBlank(message = "Latitude location cannot be empty")
    private BigDecimal latitude;
    @NotBlank(message = "Desciption cannot be empty")
    private String description;
    private String phoneNumber;
    private String email;
    private String website;
    private String facebookLink;
    private String instagramLink;
    private Boolean offerDelivery;
    private Boolean offerTakeaway;
    private Boolean outdoorSeating;
    @NotBlank(message = "User who add this restaurant cannot be empty")
    private UUID idUser;
    private List<OpenTimeDTO> openTimeList;
    private List<UUID> listIdTag;
}
