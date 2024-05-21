package com.example.foodx_be.service;

import com.example.foodx_be.dto.*;
import com.example.foodx_be.enity.Restaurant;
import com.example.foodx_be.enity.Tag;
import com.example.foodx_be.enity.UpdateRestaurant;
import com.example.foodx_be.enity.User;
import com.example.foodx_be.exception.NoResultsFoundException;
import com.example.foodx_be.repository.RestaurantRepository;
import com.example.foodx_be.repository.UpdateRestaurantRepository;
import com.example.foodx_be.ulti.BoundingBoxCalculator;
import com.example.foodx_be.ulti.Operation;
import com.example.foodx_be.ulti.RestaurantState;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
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
    public void addRestaurant(AddRestaurantCommand addRestaurantCommand) {
        User user = userService.getUser(addRestaurantCommand.getIdUser());
        Restaurant restaurant = convertToRestaurantEnity(addRestaurantCommand);
        restaurant.setUserAdd(user);
        restaurantRepository.save(restaurant);

        openTimeService.saveOpenTime(addRestaurantCommand.getOpenTimeList(), restaurant);

        List<UUID> listID = addRestaurantCommand.getListIdTag();
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


        Specification<Restaurant> restaurantSpecification = specification.getSearchSpecification(requestDTO.getSearchRequestDTO());
        Pageable pageable = new PageRequestDTO().getPageable(requestDTO.getPageRequestDTO());

        // Apply sorting based on the sortByColumn
        if ("point".equals(requestDTO.getSortByColumn())) {
            restaurantSpecification = restaurantSpecification.and(specification.sortByAverageReview(requestDTO.getSort()));
        } else {
            restaurantSpecification = restaurantSpecification.and(specification.sortByColumn(requestDTO.getSortByColumn(), requestDTO.getSort()));
        }

        Page<Restaurant> all = restaurantRepository.findAll(restaurantSpecification, pageable);
        if (all.getContent().isEmpty()) {
            throw new NoResultsFoundException();
        }
        return all.map(this::convertToRestaurantDTO);
    }


    @Override
    public Page<RestaurantDTO> getRestaurantsByKeyword(int pageNo, int limit, String keyword, String searchBy) {
        List<Restaurant> restaurantList = switch (searchBy) {
            case "city" -> restaurantRepository.findAllByCityAndRestaurantState(keyword, RestaurantState.ACTIVE);
            case "restaurantName" ->
                    restaurantRepository.findAllByRestaurantNameAndRestaurantState(keyword, RestaurantState.ACTIVE);
            default -> new ArrayList<>();
        };
        if (restaurantList.isEmpty()) {
            throw new NoResultsFoundException();
        }
        return converListRestaurantEnityToPage(restaurantList, pageNo, limit);
    }

    @Override
    public Page<RestaurantDTO> getRestaurantByRestaurantState(int pageNo, int limit, RestaurantState restaurantState) {

        List<Restaurant> restaurantList = restaurantRepository.findAllByRestaurantState(restaurantState);
        if (restaurantList.isEmpty()) {
            throw new NoResultsFoundException();
        }
        return converListRestaurantEnityToPage(restaurantList, pageNo, limit);
    }

    @Override
    public Page<RestaurantDTO> getRestaurantBySpecification(RequestDTO requestDTO) {
        requestDTO.getSearchRequestDTO().add(new SearchRequestDTO("restaurantState", RestaurantState.ACTIVE.toString(), Operation.EQUAL));
        Specification<Restaurant> restaurantSpecification = specification.getSearchSpecification(requestDTO.getSearchRequestDTO());
        Pageable pageable = new PageRequestDTO().getPageable(requestDTO.getPageRequestDTO());

        // Apply sorting based on the sortByColumn
        if ("point".equals(requestDTO.getSortByColumn())) {
            restaurantSpecification = restaurantSpecification.and(specification.sortByAverageReview(requestDTO.getSort()));
        } else {
            restaurantSpecification = restaurantSpecification.and(specification.sortByColumn(requestDTO.getSortByColumn(), requestDTO.getSort()));
        }

        Page<Restaurant> all = restaurantRepository.findAll(restaurantSpecification, pageable);
        return all.map(this::convertToRestaurantDTO);
    }

    //for checking restaurant id
    @Override
    public Restaurant getRestaurantEnity(UUID idRestaurant) {
        Optional<Restaurant> restaurantOptional = restaurantRepository.findById(idRestaurant);
        return unwrarpRestaurant(restaurantOptional);
    }

    @Override
    public Restaurant getRestaurantEnityByName(String restaurantName) {
        Optional<Restaurant> restaurantOptional = restaurantRepository.findRestaurantByRestaurantName(restaurantName);
        return unwrarpRestaurant(restaurantOptional);
    }

    @Override
    public Page<RestaurantDTO> getListRestaurantByTag(int pageNo, int limit, UUID idTag) {
        return converListRestaurantEnityToPage(restaurantTagService.getListRestaurantByTag(idTag), pageNo, limit);
    }

    @Override
    public RestaurantDTO getRestaurantDTO(UUID idRestaurant) {
        Optional<Restaurant> restaurantOptional = restaurantRepository.findById(idRestaurant);
        Restaurant restaurant = unwrarpRestaurant(restaurantOptional);
        return convertToRestaurantDTO(restaurant);
    }

    @Override
    public void updateRestaurant(UUID idRestaurant, UpdateRestaurantCommand updateRestaurantCommand) {
        User userUpdate = userService.getUser(updateRestaurantCommand.getIdUser());
        Restaurant restaurant = getRestaurantEnity(idRestaurant);

        UpdateRestaurant updateRestaurant = convertToUpdateRestaurantEnity(updateRestaurantCommand);
        updateRestaurant.setUserUpdate(userUpdate);
        updateRestaurant.setRestaurant(restaurant);
        updateRestaurantRepository.save(updateRestaurant);

        updateOpenTimeService.saveUpdateOpenTime(updateRestaurantCommand.getOpenTimeList(), updateRestaurant);

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
        else throw new NoResultsFoundException();
    }

    private Restaurant convertToRestaurantEnity(AddRestaurantCommand addRestaurantCommand) {
        return Restaurant.builder()
                .restaurantName(addRestaurantCommand.getRestaurantName())
                .houseNumber(addRestaurantCommand.getHouseNumber())
                .ward(addRestaurantCommand.getWard())
                .district(addRestaurantCommand.getDistrict())
                .city(addRestaurantCommand.getCity())
                .longitude(addRestaurantCommand.getLongitude())
                .latitude(addRestaurantCommand.getLatitude())
                .description(addRestaurantCommand.getDescription())
                .phoneNumber(addRestaurantCommand.getPhoneNumber())
                .email(addRestaurantCommand.getEmail())
                .website(addRestaurantCommand.getWebsite())
                .facebookLink(addRestaurantCommand.getFacebookLink())
                .instagramLink(addRestaurantCommand.getInstagramLink())
                .offerDelivery(addRestaurantCommand.getOfferDelivery())
                .outdoorSeating(addRestaurantCommand.getOutdoorSeating())
                .offerTakeaway(addRestaurantCommand.getOfferTakeaway())
                .build();
    }

    private UpdateRestaurant convertToUpdateRestaurantEnity(UpdateRestaurantCommand updateRestaurantCommand) {
        return UpdateRestaurant.builder()
                .restaurantName(updateRestaurantCommand.getRestaurantName())
                .houseNumber(updateRestaurantCommand.getHouseNumber())
                .ward(updateRestaurantCommand.getWard())
                .district(updateRestaurantCommand.getDistrict())
                .city(updateRestaurantCommand.getCity())
                .longitude(updateRestaurantCommand.getLongitude())
                .latitude(updateRestaurantCommand.getLatitude())
                .description(updateRestaurantCommand.getDescription())
                .phoneNumber(updateRestaurantCommand.getPhoneNumber())
                .email(updateRestaurantCommand.getEmail())
                .website(updateRestaurantCommand.getWebsite())
                .facebookLink(updateRestaurantCommand.getFacebookLink())
                .instagramLink(updateRestaurantCommand.getInstagramLink())
                .offerDelivery(updateRestaurantCommand.getOfferDelivery())
                .outdoorSeating(updateRestaurantCommand.getOutdoorSeating())
                .offerTakeaway(updateRestaurantCommand.getOfferTakeaway())
                .restaurantState(updateRestaurantCommand.getRestaurantState())
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
