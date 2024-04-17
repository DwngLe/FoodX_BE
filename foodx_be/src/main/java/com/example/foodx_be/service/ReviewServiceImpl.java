package com.example.foodx_be.service;

import com.example.foodx_be.dto.AddReviewRestaurantCommand;
import com.example.foodx_be.dto.ReviewRestaurantDTO;
import com.example.foodx_be.enity.Restaurant;
import com.example.foodx_be.enity.Review;
import com.example.foodx_be.enity.ReviewImage;
import com.example.foodx_be.enity.User;
import com.example.foodx_be.exception.NoResultsFoundException;
import com.example.foodx_be.repository.ReviewImageRepository;
import com.example.foodx_be.repository.ReviewRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
@AllArgsConstructor
public class ReviewServiceImpl implements ReviewService {
    private UserService userService;
    private RestaurantService restaurantService;
    private CloudiaryService cloudiaryService;

    private ReviewRepository reviewRepository;
    private ReviewImageRepository reviewImageRepository;

    private final String FOLDER_UPLOAD = "Review's Images";

    @Override
    public List<ReviewRestaurantDTO> getListRecentReview(int pageNo, int limit) {
        Pageable pageable = PageRequest.of(pageNo, limit);
        List<Review> reviewList = reviewRepository.findAllByOrderByReviewDateDesc(pageable);
        if(reviewList.isEmpty()){
            throw new NoResultsFoundException();
        }
        List<ReviewRestaurantDTO> reviewRestaurantDTOList = new ArrayList<>();
        for (Review review : reviewList) {
            reviewRestaurantDTOList.add(convertToReViewRestaurantDTO(review));
        }
        return reviewRestaurantDTOList;
    }

    @Override
    public void addReview(AddReviewRestaurantCommand reviewCommand, MultipartFile[] multipartFiles) throws IOException {
        User userReview = userService.getUser(reviewCommand.getUsername());
        Restaurant restaurant = restaurantService.getRestaurantEnity(reviewCommand.getRestaurantId());
        Review review = converToReviewEnity(reviewCommand);
        review.setUser(userReview);
        review.setRestaurant(restaurant);
        review.setReviewDate(LocalDateTime.now());
        reviewRepository.save(review);

        List<Map> results = cloudiaryService.uploadMultiFiles(multipartFiles, FOLDER_UPLOAD);
        for (Map result : results) {
            ReviewImage image = new ReviewImage();
            image.setImageId((String) result.get("public_id"));
            image.setName((String) result.get("original_filename"));
            image.setImageUrl((String) result.get("url"));
            image.setReview(review);
            reviewImageRepository.save(image);
        }
    }

    @Override
    public Review getReview(UUID id) {
        return reviewRepository.getOne(id);
    }

    @Override
    public Page<ReviewRestaurantDTO> getListReviewOfRestaurant(int pageNo, int limit, UUID idRestaurant) {
        List<Review> reviewList = reviewRepository.findAllByRestaurantId(idRestaurant);
        if (reviewList.isEmpty()) {
            throw new NoResultsFoundException();
        }
        List<ReviewRestaurantDTO> reviewRestaurantDTOList = new ArrayList<>();
        for (Review review : reviewList) {
            reviewRestaurantDTOList.add(convertToReViewRestaurantDTO(review));
        }
        Pageable pageable = PageRequest.of(pageNo, limit);

        int startIndex = (int) pageable.getOffset();
        int endIndex = (int) Math.min(pageable.getOffset() + pageable.getPageSize(), reviewList.size());
        List<ReviewRestaurantDTO> subList = reviewRestaurantDTOList.subList(startIndex, endIndex);
        return new PageImpl<>(subList, pageable, reviewRestaurantDTOList.size());
    }

    public Review converToReviewEnity(AddReviewRestaurantCommand addReviewCommand) {
        return Review.builder()
                .reviewTitle(addReviewCommand.getReviewTitle())
                .reviewContent(addReviewCommand.getReviewContent())
                .starNumber(addReviewCommand.getStarNumber())
                .build();
    }

    public ReviewRestaurantDTO convertToReViewRestaurantDTO(Review review) {
        return ReviewRestaurantDTO.builder()
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
