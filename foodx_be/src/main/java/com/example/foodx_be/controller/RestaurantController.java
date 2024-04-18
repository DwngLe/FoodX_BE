package com.example.foodx_be.controller;

import com.example.foodx_be.dto.AddRestaurantCommand;
import com.example.foodx_be.dto.RestaurantDTO;
import com.example.foodx_be.dto.UpdateRestaurantCommand;
import com.example.foodx_be.service.RestaurantService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.UUID;

@AllArgsConstructor
@RestController
@RequestMapping("/restaurant")
public class RestaurantController {
    private RestaurantService restaurantService;

    @PostMapping("/add")
    public ResponseEntity<HttpStatus> addRestaurant(@RequestBody AddRestaurantCommand addRestaurantCommand) {
        restaurantService.addRestaurant(addRestaurantCommand);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }


    @GetMapping("/{idRestaurant}")
    public ResponseEntity<RestaurantDTO> getRestaurant(@PathVariable UUID idRestaurant) {
        return new ResponseEntity<>(restaurantService.getRestaurantDTO(idRestaurant), HttpStatus.OK);
    }

    @GetMapping("/search")
    public ResponseEntity<Page<RestaurantDTO>> getRestaurants(@RequestParam(name = "searchBy") String searchBy,
                                                              @RequestParam(name = "keyword") String keyword,
                                                              @RequestParam(name = "pageNo", defaultValue = "0") int pageNo,
                                                              @RequestParam(name = "limit", defaultValue = "10") int limit) {
        return new ResponseEntity<>(restaurantService.getRestaurantsByKeyword(pageNo, limit, keyword, searchBy), HttpStatus.OK);
    }

    @PostMapping("/{idRestaurant}/update")
    public ResponseEntity<HttpStatus> updateRestaurant(@PathVariable UUID idRestaurant,
                                                       @RequestBody UpdateRestaurantCommand updateRestaurantCommand) {
        restaurantService.updateRestaurant(idRestaurant, updateRestaurantCommand);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/nearby")
    public ResponseEntity<Page<RestaurantDTO>> getRestaurantNearBy(@RequestParam BigDecimal longitude,
                                                                   @RequestParam BigDecimal latitude,
                                                                   @RequestParam double radiusInKm,
                                                                   @RequestParam(name = "pageNo", defaultValue = "0") int pageNo,
                                                                   @RequestParam(name = "limit", defaultValue = "10") int limit) {
        return new ResponseEntity<>(restaurantService.getNearByRestaurant(longitude, latitude, radiusInKm, pageNo, limit), HttpStatus.OK);
    }
}
