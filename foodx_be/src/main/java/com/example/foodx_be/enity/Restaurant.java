package com.example.foodx_be.enity;

import com.example.foodx_be.enums.Price;
import com.example.foodx_be.enums.RestaurantState;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Table
@Entity
@Builder
public class Restaurant {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    private UUID id;

    @Column(name = "restaurant_name")
    private String restaurantName;

    @Column(name = "house_number")
    private String houseNumber;

    private String ward;
    private String district;
    private String city;
    private BigDecimal longitude;
    private BigDecimal latitude;
    private String description;

    @Column(name = "phone_number")
    private String phoneNumber;

    private String email;
    private String website;

    @Column(name = "facebook_link")
    private String facebookLink;

    @Column(name = "instagram_link")
    private String instagramLink;

    @Column(name = "restaurant_state")
    @Enumerated(EnumType.STRING)
    private RestaurantState restaurantState;

    @Column(name = "time_added")
    private LocalDate timeAdded;

    @Column(name = "has_an_owner")
    private Boolean hasAnOwner;

    @Column(name = "review_count")
    private double reviewCount = 0;

    @Column(name = "review_sum")
    private double reviewSum = 0;

    @Column(name = "offer_delivery")
    private Boolean offerDelivery;

    @Column(name = "offer_takeaway")
    private Boolean offerTakeaway;

    @Column(name = "is_outdoor_seating")
    private Boolean outdoorSeating;

    private Price price;

    @PrePersist
    public void control() {
        if (restaurantState == null) {
            restaurantState = RestaurantState.PENDING;
        }
        if (timeAdded == null) {
            timeAdded = LocalDate.now();
        }
        if (hasAnOwner == null) {
            hasAnOwner = false;
        }
    }

    @ManyToOne
    @JoinColumn(name = "id_user_add", referencedColumnName = "id")
    private User userAdd;

    @JsonIgnore
    @OneToMany(mappedBy = "restaurant", cascade = CascadeType.ALL)
    private List<Review> reviewList;

    @JsonIgnore
    @OneToMany(mappedBy = "restaurant", cascade = CascadeType.ALL)
    private List<OpenTime> openTimeList;

    @JsonIgnore
    @OneToMany(mappedBy = "restaurant", cascade = CascadeType.ALL)
    private List<UpdateRestaurant> updateRestaurantList;

    @JsonIgnore
    @OneToMany(mappedBy = "restaurant", cascade = CascadeType.ALL)
    private List<BusinessProof> businessProofList;

    @JsonIgnore
    @OneToMany(mappedBy = "restaurant", cascade = CascadeType.ALL)
    private List<RestaurantTag> restaurantTagList;
}
