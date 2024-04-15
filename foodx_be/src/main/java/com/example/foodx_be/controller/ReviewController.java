package com.example.foodx_be.controller;

import com.example.foodx_be.dto.AddReviewRestaurantCommand;
import com.example.foodx_be.dto.ReviewRestaurantDTO;
import com.example.foodx_be.enity.Review;
import com.example.foodx_be.service.ReviewService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@AllArgsConstructor
@RequestMapping("/review")
public class ReviewController {
    private ReviewService reviewService;
    @PostMapping("/add")
    public ResponseEntity<HttpStatus> addReview(@RequestBody AddReviewRestaurantCommand addReviewCommand){
        reviewService.addReview(addReviewCommand);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping("/get/{idRestaurant}")
    public ResponseEntity<Page<ReviewRestaurantDTO>> getListReviewOfRestaurant(@PathVariable UUID idRestaurant,
                                                                               @RequestParam(name = "pageNo", defaultValue = "0") int pageNo,
                                                                               @RequestParam(name = "limit", defaultValue = "5") int limit){
        return new ResponseEntity<>(reviewService.getListReviewOfRestaurant(pageNo, limit, idRestaurant), HttpStatus.OK);
    }
}
