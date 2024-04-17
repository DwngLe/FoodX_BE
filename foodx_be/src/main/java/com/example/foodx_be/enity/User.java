package com.example.foodx_be.enity;

import com.example.foodx_be.ulti.AccountState;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import java.util.List;
import java.util.UUID;
import java.time.LocalDate;
import com.example.foodx_be.ulti.Role;

@Data
@Builder
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "user")
public class User {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    private UUID id;

    @Column(nullable = false)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String name;

    @Column(name = "phone_number", nullable = false)
    private String phoneNumber;

    @Column(nullable = false)
    private String email;

    @Column(name = "avatar_link")
    private String avatarLink;

    @Column(name = "facebook_link")
    private String facebookLink;

    @Column(name = "instagram_link")
    private String instagramLink;

    @Column(nullable = false)
    private String ward;

    @Column(nullable = false)
    private String district;

    @Column(nullable = false)
    private String city;

    @Column(name = "joint_date")
    private LocalDate jointDate;

    private int points;

    @Enumerated(EnumType.STRING)
    private Role role;
    @Enumerated(EnumType.STRING)
    private AccountState accountState;

    @PrePersist
    public void control(){
        if(role == null){
            role = Role.USER;
        }
        if(jointDate == null){
            jointDate = LocalDate.now();
        }
        if(avatarLink == null){
            avatarLink = "https://res.cloudinary.com/dfsdwobb1/image/upload/v1713339322/Review%27s%20Images/gpft7enbgnz1wm2xnd4i.png";
        }
        if(accountState == null){
            accountState = AccountState.ACTIVE;
        }
    }

    @JsonIgnore
    @OneToMany(mappedBy = "userSent", cascade = CascadeType.ALL)
    private List<Message> sentMessageList;

    @JsonIgnore
    @OneToMany(mappedBy = "userRecvie", cascade = CascadeType.ALL)
    private List<Message> reciveMessageList;

    @JsonIgnore
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Review>   reviewList;

    @JsonIgnore
    @OneToMany(mappedBy = "userAdd", cascade = CascadeType.ALL)
    private List<Restaurant> restaurantList;

    @JsonIgnore
    @OneToMany(mappedBy = "userUpdate", cascade = CascadeType.ALL)
    private List<UpdateRestaurant> updateRestaurantList;

}
