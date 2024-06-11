package com.example.foodx_be.enity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table
public class ReviewImage {
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
    @JoinColumn(name = "id_review", referencedColumnName = "id")
    @JsonIgnore
    private Review review;
}
