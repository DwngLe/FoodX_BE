package com.example.foodx_be.service;

import com.example.foodx_be.dto.request.Request;
import com.example.foodx_be.dto.request.RestaurantCreationRequest;
import com.example.foodx_be.dto.request.RestaurantUpdateRequest;
import com.example.foodx_be.dto.response.*;
import com.example.foodx_be.enity.*;
import com.example.foodx_be.enums.GlobalOperator;
import com.example.foodx_be.enums.Operation;
import com.example.foodx_be.enums.RestaurantState;
import com.example.foodx_be.exception.AppException;
import com.example.foodx_be.exception.ErrorCode;
import com.example.foodx_be.mapper.RestaurantImageMapper;
import com.example.foodx_be.mapper.RestaurantMapper;
import com.example.foodx_be.mapper.UpdateRestaurantMapper;
import com.example.foodx_be.repository.RestaurantImageRepository;
import com.example.foodx_be.repository.RestaurantRepository;
import com.example.foodx_be.repository.UpdateRestaurantRepository;
import com.example.foodx_be.ulti.BoundingBoxCalculator;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class RestaurantServiceImpl implements RestaurantService {
    private RestaurantMapper restaurantMapper;
    private RestaurantImageMapper restaurantImageMapper;
    private UpdateRestaurantMapper updateRestaurantMapper;

    private UserService userService;
    private OpenTimeService openTimeService;
    private UpdateOpenTimeService updateOpenTimeService;
    private TagService tagService;
    private RestaurantTagService restaurantTagService;
    private CloudiaryService cloudiaryService;

    private FiltersSpecificationImpl<Restaurant> specification;

    private RestaurantRepository restaurantRepository;
    private UpdateRestaurantRepository updateRestaurantRepository;
    private RestaurantImageRepository restaurantImageRepository;

    private final String FOLDER_UPLOAD = "Restaurant's Images";

    @Override
    public void addRestaurant(RestaurantCreationRequest restaurantCreationRequest, MultipartFile[] multipartFiles) throws IOException {
        var context = SecurityContextHolder.getContext();
        User user =
                userService.getUser(UUID.fromString(context.getAuthentication().getName()));
        Restaurant restaurant = convertToRestaurantEnity(restaurantCreationRequest);
        restaurant.setUserAdd(user);
        restaurantRepository.save(restaurant);

//        openTimeService.saveOpenTime(restaurantCreationRequest.getOpenTimeList(), restaurant);

        List<UUID> listID = restaurantCreationRequest.getListIdTag();
        for (UUID uuid : listID) {
            Tag tag = tagService.getTagEity(uuid);
            restaurantTagService.saveRestaurantTag(restaurant, tag);
        }

        if (multipartFiles != null) {
            List<Map> results = cloudiaryService.uploadMultiFiles(multipartFiles, FOLDER_UPLOAD);
            for (Map result : results) {
                RestaurantImage image = new RestaurantImage();
                image.setImageId((String) result.get("public_id"));
                image.setName((String) result.get("original_filename"));
                image.setImageUrl((String) result.get("url"));
                image.setRestaurant(restaurant);
                restaurantImageRepository.save(image);
            }
        }
    }

    @Override
    public void saveRestaurantEnity(Restaurant restaurant) {
        restaurantRepository.save(restaurant);
    }

    @Override
    public Page<RestaurantBasicInfoResponse> getNearByRestaurant(Request request, LocationRequest locationRequest) {
        BigDecimal latitude = locationRequest.getLatitude();
        BigDecimal longitude = locationRequest.getLongitude();
        BigDecimal radiusInKm = locationRequest.getRadius();

        BigDecimal[] boundingBox = BoundingBoxCalculator.calculateBoundingBox(latitude, longitude, radiusInKm);

        // min lat, min long, max lat, max log
        request
                .getSearchRequestDTO()
                .add(new SearchRequestDTO(
                        "latitude", boundingBox[0].toString() + ", " + boundingBox[2], Operation.BETWEEN));
        request
                .getSearchRequestDTO()
                .add(new SearchRequestDTO(
                        "longitude", boundingBox[1].toString() + ", " + boundingBox[3], Operation.BETWEEN));
        request
                .getSearchRequestDTO()
                .add(new SearchRequestDTO("restaurantState", RestaurantState.ACTIVE.toString(), Operation.EQUAL));

        Specification<Restaurant> restaurantSpecification =
                specification.getSearchSpecification(request.getSearchRequestDTO(), GlobalOperator.AND);
        Pageable pageable = new PageRequestDTO().getPageable(request.getPageRequestDTO());

        // Apply sorting based on the sortByColumn
        if ("point".equals(request.getSortByColumn())) {
            restaurantSpecification =
                    restaurantSpecification.and(specification.sortByAverageReview(request.getSort()));
        } else {
            restaurantSpecification = restaurantSpecification.and(
                    specification.sortByColumn(request.getSortByColumn(), request.getSort()));
        }

        Page<Restaurant> all = restaurantRepository.findAll(restaurantSpecification, pageable);
        if (all.getContent().isEmpty()) {
            throw new AppException(ErrorCode.RESTAURANT_NOT_EXISTED);
        }
        return all.map(this::convertToRestaurantBasicInfo);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Override
    public Page<RestaurantBasicInfoResponse> getRestaurantByRestaurantState(int pageNo, int limit, RestaurantState restaurantState) {
        Pageable pageable = PageRequest.of(pageNo, limit);
        Page<Restaurant> restaurantPage = restaurantRepository.findAllByRestaurantState(restaurantState, pageable);
        if (restaurantPage.isEmpty()) {
            throw new AppException(ErrorCode.RESTAURANT_NOT_EXISTED);
        }
        return restaurantPage.map(this::convertToRestaurantBasicInfo);
    }

    @Override
    public Page<RestaurantBasicInfoResponse> getRestaurantBySpecification(Request request) {
        request
                .getSearchRequestDTO()
                .add(new SearchRequestDTO("restaurantState", RestaurantState.ACTIVE.toString(), Operation.EQUAL));
        Specification<Restaurant> restaurantSpecification =
                specification.getSearchSpecification(request.getSearchRequestDTO(), GlobalOperator.AND);
        Pageable pageable = new PageRequestDTO().getPageable(request.getPageRequestDTO());

        // Apply sorting based on the sortByColumn
        if ("point".equals(request.getSortByColumn())) {
            restaurantSpecification =
                    restaurantSpecification.and(specification.sortByAverageReview(request.getSort()));
        } else {
            restaurantSpecification = restaurantSpecification.and(
                    specification.sortByColumn(request.getSortByColumn(), request.getSort()));
        }

        Page<Restaurant> all = restaurantRepository.findAll(restaurantSpecification, pageable);
        if (all.getContent().isEmpty()) {
            throw new AppException(ErrorCode.RESTAURANT_NOT_EXISTED);
        }
        return all.map(this::convertToRestaurantBasicInfo);
    }

    // for checking restaurant id
    @Override
    public Restaurant getRestaurantEnity(UUID idRestaurant) {
        Optional<Restaurant> restaurantOptional = restaurantRepository.findById(idRestaurant);
        return unwrarpRestaurant(restaurantOptional);
    }

    public Page<RestaurantTag> getListRestaurantByTag(Request request) {
        return restaurantTagService.getListRestaurantByTag(request);
    }

    @Override
    public RestaurantResponse getRestaurantDTO(UUID idRestaurant) {
        Optional<Restaurant> restaurantOptional = restaurantRepository.findById(idRestaurant);
        Restaurant restaurant = unwrarpRestaurant(restaurantOptional);
        return convertToRestaurantResponse(restaurant);
    }

    @Override
    public void updateRestaurant(UUID idRestaurant, RestaurantUpdateRequest restaurantUpdateRequest) {
        var context = SecurityContextHolder.getContext();
        User userUpdate =
                userService.getUser(UUID.fromString(context.getAuthentication().getName()));
        Restaurant restaurant = getRestaurantEnity(idRestaurant);

        UpdateRestaurant updateRestaurant = convertToUpdateRestaurantEnity(restaurantUpdateRequest);
        updateRestaurant.setUserUpdate(userUpdate);
        updateRestaurant.setRestaurant(restaurant);
        updateRestaurantRepository.save(updateRestaurant);

        if (restaurantUpdateRequest.getOpenTimeList() != null) {
            updateOpenTimeService.saveUpdateOpenTime(restaurantUpdateRequest.getOpenTimeList(), updateRestaurant);
        }
    }

    @Override
    public Restaurant updateRestaurantPoint(UUID idRestaurant, double point, double count) {
        Restaurant restaurant = unwrarpRestaurant(restaurantRepository.findById(idRestaurant));
        restaurant.setReviewCount(restaurant.getReviewCount() + count);
        restaurant.setReviewSum(restaurant.getReviewSum() + point);
        restaurantRepository.save(restaurant);
        return restaurant;
    }

    public Page<RestaurantResponse> converListRestaurantEnityToPage(List<Restaurant> restaurants, int pageNo, int limit) {
        List<RestaurantResponse> restaurantResponseList = new ArrayList<>();
        for (Restaurant restaurant : restaurants) {
            restaurantResponseList.add(convertToRestaurantResponse(restaurant));
        }
        Pageable pageable = PageRequest.of(pageNo, limit);

        int startIndex = (int) pageable.getOffset();
        int endIndex = (int) Math.min(pageable.getOffset() + pageable.getPageSize(), restaurantResponseList.size());
        List<RestaurantResponse> subList = restaurantResponseList.subList(startIndex, endIndex);
        return new PageImpl<>(subList, pageable, restaurantResponseList.size());
    }

    static Restaurant unwrarpRestaurant(Optional<Restaurant> entity) {
        if (entity.isPresent()) return entity.get();
        else throw new AppException(ErrorCode.RESTAURANT_NOT_EXISTED);
    }

    private Restaurant convertToRestaurantEnity(RestaurantCreationRequest restaurantCreationRequest) {
        return Restaurant.builder()
                .restaurantName(restaurantCreationRequest.getRestaurantName())
                .houseNumber(restaurantCreationRequest.getHouseNumber())
                .ward(restaurantCreationRequest.getWard())
                .district(restaurantCreationRequest.getDistrict())
                .city(restaurantCreationRequest.getCity())
                .longitude(restaurantCreationRequest.getLongitude())
                .latitude(restaurantCreationRequest.getLatitude())
                .description(restaurantCreationRequest.getDescription())
                .phoneNumber(restaurantCreationRequest.getPhoneNumber())
                .email(restaurantCreationRequest.getEmail())
                .website(restaurantCreationRequest.getWebsite())
                .facebookLink(restaurantCreationRequest.getFacebookLink())
                .instagramLink(restaurantCreationRequest.getInstagramLink())
                .offerDelivery(restaurantCreationRequest.getOfferDelivery())
                .outdoorSeating(restaurantCreationRequest.getOutdoorSeating())
                .offerTakeaway(restaurantCreationRequest.getOfferTakeaway())
                .build();
    }

    private UpdateRestaurant convertToUpdateRestaurantEnity(RestaurantUpdateRequest restaurantUpdateRequest) {
        return UpdateRestaurant.builder()
                .restaurantName(restaurantUpdateRequest.getRestaurantName())
                .houseNumber(restaurantUpdateRequest.getHouseNumber())
                .ward(restaurantUpdateRequest.getWard())
                .district(restaurantUpdateRequest.getDistrict())
                .city(restaurantUpdateRequest.getCity())
                .longitude(restaurantUpdateRequest.getLongitude())
                .latitude(restaurantUpdateRequest.getLatitude())
                .description(restaurantUpdateRequest.getDescription())
                .phoneNumber(restaurantUpdateRequest.getPhoneNumber())
                .email(restaurantUpdateRequest.getEmail())
                .website(restaurantUpdateRequest.getWebsite())
                .facebookLink(restaurantUpdateRequest.getFacebookLink())
                .instagramLink(restaurantUpdateRequest.getInstagramLink())
                .offerDelivery(restaurantUpdateRequest.getOfferDelivery())
                .outdoorSeating(restaurantUpdateRequest.getOutdoorSeating())
                .offerTakeaway(restaurantUpdateRequest.getOfferTakeaway())
                .restaurantState(restaurantUpdateRequest.getRestaurantState())
                .build();
    }

    private RestaurantResponse convertToRestaurantResponse(Restaurant restaurant) {
        RestaurantResponse.RestaurantResponseBuilder builder = RestaurantResponse.builder()
                .id(restaurant.getId())
                .restaurantName(restaurant.getRestaurantName())
                .description(restaurant.getDescription())
                .houseNumber(restaurant.getHouseNumber())
                .ward(restaurant.getWard())
                .district(restaurant.getDistrict())
                .city(restaurant.getCity())
                .longitude(restaurant.getLongitude())
                .latitude(restaurant.getLatitude())
                .description(restaurant.getDescription())
                .phoneNumber(restaurant.getPhoneNumber())
                .email(restaurant.getEmail())
                .website(restaurant.getWebsite())
                .facebookLink(restaurant.getFacebookLink())
                .instagramLink(restaurant.getInstagramLink())
                .offerDelivery(restaurant.getOfferDelivery())
                .outdoorSeating(restaurant.getOutdoorSeating())
                .offerTakeaway(restaurant.getOfferTakeaway())
                .restaurantState(restaurant.getRestaurantState())
                .timeAdded(restaurant.getTimeAdded())
                .hasAnOwner(restaurant.getHasAnOwner())
                .userAdd(userService.convertTouserBasicInfor(restaurant.getUserAdd()))
                .points(restaurant.getReviewCount() == 0 ? 0 : restaurant.getReviewSum() / restaurant.getReviewCount())
                .restaurantImageResponseList(restaurant.getRestaurantImageList().stream().map(restaurantImageMapper::toRestaurantImageResponse).collect(Collectors.toList()))
                .tagDTOList(tagService.convertToListTagDTO(
                        restaurantTagService.getListTagOfRestaurant(restaurant.getId())));
        if (restaurant.getUserAdd() != null) {
            builder.userAdd(userService.convertTouserBasicInfor(restaurant.getUserAdd()));
        }
        return builder.build();
    }

    private RestaurantBasicInfoResponse convertToRestaurantBasicInfo(Restaurant restaurant) {
        RestaurantBasicInfoResponse.RestaurantBasicInfoResponseBuilder builder = RestaurantBasicInfoResponse.builder()
                .id(restaurant.getId())
                .restaurantName(restaurant.getRestaurantName())
                .description(restaurant.getDescription())
                .offerDelivery(restaurant.getOfferDelivery())
                .outdoorSeating(restaurant.getOutdoorSeating())
                .offerTakeaway(restaurant.getOfferTakeaway())
                .houseNumber(restaurant.getHouseNumber())
                .ward(restaurant.getWard())
                .district(restaurant.getDistrict())
                .city(restaurant.getCity())
                .longitude(restaurant.getLongitude())
                .latitude(restaurant.getLatitude())
                .points(restaurant.getReviewCount() == 0 ? 0 : restaurant.getReviewSum() / restaurant.getReviewCount())
                .restaurantImageResponseList(restaurant.getRestaurantImageList().stream().map(restaurantImageMapper::toRestaurantImageResponse).collect(Collectors.toList()))
                .tagDTOList(tagService.convertToListTagDTO(
                        restaurantTagService.getListTagOfRestaurant(restaurant.getId())));
        return builder.build();
    }
}
