package com.example.foodx_be.enity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table
@Builder
public class UpdateOpenTime {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    private UUID id;
    @NotBlank
    @Column(name = "day_of_week")
    private String dayOfWeek;
    @Column(name = "opening_time")
    private String openingTime;
    @Column(name = "closing_time")
    private String closingTime;

    @ManyToOne(optional = false)
    @JoinColumn(name = "id_update_restaurant", referencedColumnName = "id")
    private UpdateRestaurant updateRestaurant;
}
