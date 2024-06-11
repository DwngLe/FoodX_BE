package com.example.foodx_be.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.foodx_be.enity.UpdateRestaurant;
import com.example.foodx_be.enums.UpdateState;

public interface UpdateRestaurantRepository extends JpaRepository<UpdateRestaurant, UUID> {
    List<UpdateRestaurant> findAllByUpdateState(UpdateState updateState);
}
