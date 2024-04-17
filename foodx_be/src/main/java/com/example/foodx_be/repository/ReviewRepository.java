package com.example.foodx_be.repository;

import com.example.foodx_be.enity.Review;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface ReviewRepository extends JpaRepository<Review, UUID> {
    List<Review> findAllByRestaurantId(UUID idRestaurant);
    List<Review> findAllByOrderByReviewDateDesc(Pageable pageable);
}
