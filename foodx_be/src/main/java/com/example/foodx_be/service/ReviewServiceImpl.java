package com.example.foodx_be.service;

import com.example.foodx_be.dto.request.ReviewRestaurantCreationRequest;
import com.example.foodx_be.dto.response.PageRequestDTO;
import com.example.foodx_be.dto.response.RequestDTO;
import com.example.foodx_be.dto.response.ReviewRestaurantDTO;
import com.example.foodx_be.dto.response.SearchRequestDTO;
import com.example.foodx_be.enity.Restaurant;
import com.example.foodx_be.enity.Review;
import com.example.foodx_be.enity.ReviewImage;
import com.example.foodx_be.enity.User;
import com.example.foodx_be.exception.AppException;
import com.example.foodx_be.exception.ErrorCode;
import com.example.foodx_be.repository.ReviewImageRepository;
import com.example.foodx_be.repository.ReviewRepository;
import com.example.foodx_be.ulti.GlobalOperator;
import com.example.foodx_be.ulti.Operation;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.context.SecurityContextHolder;
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
    private FiltersSpecificationImpl<Review> specification;

    private ReviewRepository reviewRepository;
    private ReviewImageRepository reviewImageRepository;

    private final String FOLDER_UPLOAD = "Review's Images";
    private final int POINTS_REVIEW = 5;
    private final double REVIEW_ADD_COUNT = 1;
    private final double REVIEW_DELETE_COUNT = -1;

    @Override
    public List<ReviewRestaurantDTO> getListRecentReview(int pageNo, int limit) {
        Pageable pageable = PageRequest.of(pageNo, limit);
        List<Review> reviewList = reviewRepository.findAllByOrderByReviewDateDesc(pageable);
        if (reviewList.isEmpty()) {
            throw new AppException(ErrorCode.RESTAURANT_NOT_EXISTED);
        }
        List<ReviewRestaurantDTO> reviewRestaurantDTOList = new ArrayList<>();
        for (Review review : reviewList) {
            reviewRestaurantDTOList.add(convertToReViewRestaurantDTO(review));
        }
        return reviewRestaurantDTOList;
    }

    @Override
    public void addReview(ReviewRestaurantCreationRequest reviewCommand, MultipartFile[] multipartFiles) throws IOException {
        //update user and restaurant point
        var context = SecurityContextHolder.getContext();
        UUID idUser = UUID.fromString(context.getAuthentication().getName());
        User userReview = userService.updateUserPoint(idUser, POINTS_REVIEW);
        Restaurant restaurant = restaurantService.updateRestaurantPoint(reviewCommand.getRestaurantId(), reviewCommand.getStarNumber(), REVIEW_ADD_COUNT);

        //add review
        Review review = converToReviewEnity(reviewCommand);
        review.setUser(userReview);
        review.setRestaurant(restaurant);
        review.setReviewDate(LocalDateTime.now());
        reviewRepository.save(review);


        if (multipartFiles != null) {
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
    }


    @Override
    public Page<ReviewRestaurantDTO> getListReviewBySpecification(RequestDTO requestDTO) {
        Specification<Review> reviewSpecification = specification.getReviewSpecification(requestDTO.getSearchRequestDTO(), GlobalOperator.AND);
        Pageable pageable = new PageRequestDTO().getPageable(requestDTO.getPageRequestDTO());
        reviewSpecification.and(specification.sortByColumn(requestDTO.getSortByColumn(), requestDTO.getSort()));
        Page<Review> all = reviewRepository.findAll(reviewSpecification, pageable);
        if (all.getContent().isEmpty()) {
            throw new AppException(ErrorCode.REVIEW_NOT_EXISTED);
        }
        return all.map(this::convertToReViewRestaurantDTO);
    }

    @Override
    public Page<ReviewRestaurantDTO> getMyReviewSpecification(RequestDTO requestDTO) {
        var context = SecurityContextHolder.getContext();
        UUID userId = UUID.fromString(context.getAuthentication().getName());
        requestDTO.getSearchRequestDTO().add(new SearchRequestDTO("userId", userId.toString(), Operation.EQUAL));
        Specification<Review> reviewSpecification = specification.getReviewSpecification(requestDTO.getSearchRequestDTO(), GlobalOperator.AND);
        Pageable pageable = new PageRequestDTO().getPageable(requestDTO.getPageRequestDTO());
        reviewSpecification.and(specification.sortByColumn(requestDTO.getSortByColumn(), requestDTO.getSort()));
        Page<Review> all = reviewRepository.findAll(reviewSpecification, pageable);
        if (all.getContent().isEmpty()) {
            throw new AppException(ErrorCode.REVIEW_NOT_EXISTED);
        }
        return all.map(this::convertToReViewRestaurantDTO);
    }

    public Review converToReviewEnity(ReviewRestaurantCreationRequest addReviewCommand) {
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
                .userReview(userService.convertTouserBasicInfor(review.getUser()))
                .restaurantId(review.getRestaurant().getId())
                .reviewImageIds(review.getReviewImageList())
                .build();
    }
}
