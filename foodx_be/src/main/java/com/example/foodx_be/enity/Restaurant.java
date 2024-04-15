package com.example.foodx_be.enity;

import com.example.foodx_be.ulti.RestaurantState;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
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
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
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
    private RestaurantState restaurantState;
    @Column(name = "time_added")
    private LocalDate timeAdded;


    @ManyToOne
    @JoinColumn(name = "id_user_add", referencedColumnName = "id")
    private User userAdd;

    @ManyToOne
    @JoinColumn(name = "id_user_owner", referencedColumnName = "id")
    private User userOwner;

    @PrePersist
    public void control() {
        if (restaurantState == null) {
            restaurantState = RestaurantState.PENDING;
        }
        if (timeAdded == null) {
            timeAdded = LocalDate.now();
        }
    }


    @JsonIgnore
    @OneToMany(mappedBy = "restaurant", cascade = CascadeType.ALL)
    private List<Review> reviewList;

    @JsonIgnore
    @OneToMany(mappedBy = "restaurant", cascade = CascadeType.ALL)
    private List<OpenTime> openTimeList;

    @JsonIgnore
    @OneToMany(mappedBy = "restaurant", cascade = CascadeType.ALL)
    private List<UpdateRestaurant> updateRestaurantList;

}
