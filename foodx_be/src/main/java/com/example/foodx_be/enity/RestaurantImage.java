package com.example.foodx_be.enity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Table
@Entity
@Builder
public class RestaurantImage {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    private UUID id;

    @JsonIgnore
    @Column(name = "image_id")
    private String imageId;

    private String name;

    @Column(name = "image_url")
    private String imageUrl;

    @ManyToOne(optional = true)
    @JoinColumn(name = "id_restaurant", referencedColumnName = "id")
    @JsonIgnore
    private Restaurant restaurant;
}
