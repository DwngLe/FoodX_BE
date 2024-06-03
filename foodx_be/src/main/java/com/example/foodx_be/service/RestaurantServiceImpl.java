package com.example.foodx_be.service;

import com.example.foodx_be.dto.request.RestaurantCreationRequest;
import com.example.foodx_be.dto.request.RestaurantUpdateRequest;
import com.example.foodx_be.dto.response.*;
import com.example.foodx_be.enity.*;
import com.example.foodx_be.exception.AppException;
import com.example.foodx_be.exception.ErrorCode;
import com.example.foodx_be.repository.RestaurantRepository;
import com.example.foodx_be.repository.UpdateRestaurantRepository;
import com.example.foodx_be.ulti.BoundingBoxCalculator;
import com.example.foodx_be.ulti.GlobalOperator;
import com.example.foodx_be.ulti.Operation;
import com.example.foodx_be.ulti.RestaurantState;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
public class RestaurantServiceImpl implements RestaurantService {
    private UserService userService;
    private OpenTimeService openTimeService;
    private UpdateOpenTimeService updateOpenTimeService;
    private TagService tagService;
    private RestaurantTagService restaurantTagService;

    private FiltersSpecificationImpl<Restaurant> specification;

    private RestaurantRepository restaurantRepository;
    private UpdateRestaurantRepository updateRestaurantRepository;

    @Override
    public void addRestaurant(RestaurantCreationRequest restaurantCreationRequest) {
        var context = SecurityContextHolder.getContext();
        User user = userService.getUser(UUID.fromString(context.getAuthentication().getName()));
        Restaurant restaurant = convertToRestaurantEnity(restaurantCreationRequest);
        restaurant.setUserAdd(user);
        restaurantRepository.save(restaurant);

        openTimeService.saveOpenTime(restaurantCreationRequest.getOpenTimeList(), restaurant);

        List<UUID> listID = restaurantCreationRequest.getListIdTag();
        for (UUID uuid : listID) {
            Tag tag = tagService.getTagEity(uuid);
            restaurantTagService.saveRestaurantTag(restaurant, tag);
        }
    }

    @Override
    public void saveRestaurantEnity(Restaurant restaurant) {
        restaurantRepository.save(restaurant);
    }

    @Override
    public Page<RestaurantDTO> getNearByRestaurant(RequestDTO requestDTO, LocationDTO locationDTO) {
        //random number
        BigDecimal latitude = locationDTO.getLatitude();
        BigDecimal longitude = locationDTO.getLongitude();
        BigDecimal radiusInKm = locationDTO.getRadius();


        BigDecimal[] boundingBox = BoundingBoxCalculator.calculateBoundingBox(latitude, longitude, radiusInKm);

        //min lat, min long, max lat, max log
        requestDTO.getSearchRequestDTO().add(new SearchRequestDTO("latitude", boundingBox[0].toString() + ", " + boundingBox[2], Operation.BETWEEN));
        requestDTO.getSearchRequestDTO().add(new SearchRequestDTO("longitude", boundingBox[1].toString() + ", " + boundingBox[3], Operation.BETWEEN));
        requestDTO.getSearchRequestDTO().add(new SearchRequestDTO("restaurantState", RestaurantState.ACTIVE.toString(), Operation.EQUAL));


        Specification<Restaurant> restaurantSpecification = specification.getSearchSpecification(requestDTO.getSearchRequestDTO(), GlobalOperator.AND);
        Pageable pageable = new PageRequestDTO().getPageable(requestDTO.getPageRequestDTO());

        // Apply sorting based on the sortByColumn
        if ("point".equals(requestDTO.getSortByColumn())) {
            restaurantSpecification = restaurantSpecification.and(specification.sortByAverageReview(requestDTO.getSort()));
        } else {
            restaurantSpecification = restaurantSpecification.and(specification.sortByColumn(requestDTO.getSortByColumn(), requestDTO.getSort()));
        }

        Page<Restaurant> all = restaurantRepository.findAll(restaurantSpecification, pageable);
        if (all.getContent().isEmpty()) {
            throw new AppException(ErrorCode.RESTAURANT_NOT_EXISTED);
        }
        return all.map(this::convertToRestaurantDTO);
    }



    @Override
    public Page<RestaurantDTO> getRestaurantByRestaurantState(int pageNo, int limit, RestaurantState restaurantState) {

        List<Restaurant> restaurantList = restaurantRepository.findAllByRestaurantState(restaurantState);
        if (restaurantList.isEmpty()) {
            throw new AppException(ErrorCode.RESTAURANT_NOT_EXISTED);
        }
        return converListRestaurantEnityToPage(restaurantList, pageNo, limit);
    }

    @Override
    public Page<RestaurantDTO> getRestaurantBySpecification(RequestDTO requestDTO) {
        requestDTO.getSearchRequestDTO().add(new SearchRequestDTO("restaurantState", RestaurantState.ACTIVE.toString(), Operation.EQUAL));
        Specification<Restaurant> restaurantSpecification = specification.getSearchSpecification(requestDTO.getSearchRequestDTO(), GlobalOperator.AND);
        Pageable pageable = new PageRequestDTO().getPageable(requestDTO.getPageRequestDTO());

        // Apply sorting based on the sortByColumn
        if ("point".equals(requestDTO.getSortByColumn())) {
            restaurantSpecification = restaurantSpecification.and(specification.sortByAverageReview(requestDTO.getSort()));
        } else {
            restaurantSpecification = restaurantSpecification.and(specification.sortByColumn(requestDTO.getSortByColumn(), requestDTO.getSort()));
        }

        Page<Restaurant> all = restaurantRepository.findAll(restaurantSpecification, pageable);
        if (all.getContent().isEmpty()) {
            throw new AppException(ErrorCode.RESTAURANT_NOT_EXISTED);
        }
        return all.map(this::convertToRestaurantDTO);
    }

    //for checking restaurant id
    @Override
    public Restaurant getRestaurantEnity(UUID idRestaurant) {
        Optional<Restaurant> restaurantOptional = restaurantRepository.findById(idRestaurant);
        return unwrarpRestaurant(restaurantOptional);
    }


    public Page<RestaurantTag> getListRestaurantByTag(RequestDTO requestDTO) {
        return restaurantTagService.getListRestaurantByTag(requestDTO);
    }

    @Override
    public RestaurantDTO getRestaurantDTO(UUID idRestaurant) {
        Optional<Restaurant> restaurantOptional = restaurantRepository.findById(idRestaurant);
        Restaurant restaurant = unwrarpRestaurant(restaurantOptional);
        return convertToRestaurantDTO(restaurant);
    }

    @Override
    public void updateRestaurant(UUID idRestaurant, RestaurantUpdateRequest restaurantUpdateRequest) {
        var context = SecurityContextHolder.getContext();
        User userUpdate = userService.getUser(UUID.fromString(context.getAuthentication().getName()));
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


    public Page<RestaurantDTO> converListRestaurantEnityToPage(List<Restaurant> restaurants, int pageNo, int limit) {
        List<RestaurantDTO> restaurantDTOList = new ArrayList<>();
        for (Restaurant restaurant : restaurants) {
            restaurantDTOList.add(convertToRestaurantDTO(restaurant));
        }
        Pageable pageable = PageRequest.of(pageNo, limit);

        int startIndex = (int) pageable.getOffset();
        int endIndex = (int) Math.min(pageable.getOffset() + pageable.getPageSize(), restaurantDTOList.size());
        List<RestaurantDTO> subList = restaurantDTOList.subList(startIndex, endIndex);
        return new PageImpl<>(subList, pageable, restaurantDTOList.size());
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

    private RestaurantDTO convertToRestaurantDTO(Restaurant restaurant) {
        RestaurantDTO.RestaurantDTOBuilder builder = RestaurantDTO.builder()
                .id(restaurant.getId())
                .restaurantName(restaurant.getRestaurantName())
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
                .points(restaurant.getReviewSum() / restaurant.getReviewCount())
                .tagDTOList(tagService.convertToListTagDTO(restaurantTagService.getListTagOfRestaurant(restaurant.getId())));
        if (restaurant.getUserAdd() != null) {
            builder.userAdd(userService.convertTouserBasicInfor(restaurant.getUserAdd()));
        }
        return builder.build();
    }


}
