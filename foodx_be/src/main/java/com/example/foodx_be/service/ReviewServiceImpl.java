package com.example.foodx_be.service;

import com.example.foodx_be.dto.AddReviewRestaurantCommand;
import com.example.foodx_be.dto.ReviewRestaurantDTO;
import com.example.foodx_be.enity.Restaurant;
import com.example.foodx_be.enity.Review;
import com.example.foodx_be.enity.User;
import com.example.foodx_be.exception.NoResultsFoundException;
import com.example.foodx_be.repository.ReviewRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
@Service
@AllArgsConstructor
public class ReviewServiceImpl implements ReviewService{
    private UserService userService;
    private RestaurantService restaurantService;

    private ReviewRepository reviewRepository;
    @Override
    public void addReview(AddReviewRestaurantCommand reviewCommand) {
        User userReview = userService.getUser(reviewCommand.getUsername());
        Restaurant restaurant = restaurantService.getRestaurantEnity(reviewCommand.getRestaurantId());
        Review review = converToReviewEnity(reviewCommand);
        review.setUser(userReview);
        review.setRestaurant(restaurant);
        reviewRepository.save(review);
    }

    @Override
    public Review getReview(UUID id) {
        return reviewRepository.getOne(id);
    }

    @Override
    public Page<ReviewRestaurantDTO> getListReviewOfRestaurant(int pageNo, int limit, UUID idRestaurant) {
        List<Review> reviewList = reviewRepository.findAllByRestaurantId(idRestaurant);
        if(reviewList.isEmpty()){
            throw  new NoResultsFoundException();
        }
        List<ReviewRestaurantDTO> reviewRestaurantDTOList = new ArrayList<>();
        for(Review review : reviewList){
            reviewRestaurantDTOList.add(convertToReViewRestaurantDTO(review));
        }
        Pageable pageable = PageRequest.of(pageNo, limit);

        int startIndex = (int) pageable.getOffset();
        int endIndex = (int)Math.min(pageable.getOffset() + pageable.getPageSize(), reviewList.size());
        List<ReviewRestaurantDTO> subList = reviewRestaurantDTOList.subList(startIndex, endIndex);
        return new PageImpl<>(subList, pageable, reviewRestaurantDTOList.size());
    }

    public Review converToReviewEnity(AddReviewRestaurantCommand addReviewCommand){
        return Review.builder()
                .reviewTitle(addReviewCommand.getReviewTitle())
                .reviewContent(addReviewCommand.getReviewContent())
                .starNumber(addReviewCommand.getStarNumber())
                .build();
    }

    public ReviewRestaurantDTO convertToReViewRestaurantDTO(Review review){
        return  ReviewRestaurantDTO.builder()
                .id(review.getId())
                .reviewDate(review.getReviewDate())
                .reviewTitle(review.getReviewTitle())
                .reviewContent(review.getReviewContent())
                .starNumber(review.getStarNumber())
                .likeNumber(review.getLikeNumber())
                .unlikeNumber(review.getUnlikeNumber())
                .userId(userService.convertToDTO(review.getUser()))// Ví dụ về cách tạo một UserDTO từ User
                .restaurantId(review.getRestaurant().getId())
                .reviewImageIds(review.getReviewImageList())
                .build();
    }
}
