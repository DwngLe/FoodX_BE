package com.example.foodx_be.repository;

import com.example.foodx_be.enity.Restaurant;
import com.example.foodx_be.ulti.RestaurantState;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface RestaurantRepository extends JpaRepository<Restaurant, UUID>, JpaSpecificationExecutor<Restaurant> {
    Optional<Restaurant> findRestaurantByRestaurantName(String restaurantName);
    List<Restaurant> findAllByRestaurantNameAndRestaurantState(String retaurantName,  RestaurantState restaurantState);
    List<Restaurant> findAllByRestaurantState(RestaurantState restaurantState);

    @Query(value = "select a from Restaurant a " +
            "where a.city = :city and a.restaurantState = :restaurantState " +
            "order by (a.reviewSum/a.reviewCount) desc ")
    List<Restaurant> findAllByCityAndRestaurantState(String city, RestaurantState restaurantState);
    List<Restaurant> findAllByDistrict(String district);

    @Query(value = "SELECT r FROM Restaurant r " +
            "WHERE r.latitude BETWEEN ?1 AND ?3 AND r.longitude BETWEEN ?2 AND ?4 " +
            "order by (r.reviewCount/r.reviewSum) desc ")
    List<Restaurant> findRestaurantsWithinBoundingBox(double minLat, double minLng, double maxLat, double maxLng);
}
