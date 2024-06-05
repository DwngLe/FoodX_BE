package com.example.foodx_be.repository;

import com.example.foodx_be.enity.UpdateRestaurant;
import com.example.foodx_be.enums.UpdateState;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface UpdateRestaurantRepository extends JpaRepository<UpdateRestaurant, UUID> {
    List<UpdateRestaurant> findAllByUpdateState(UpdateState updateState);
}
