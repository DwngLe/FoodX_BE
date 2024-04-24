package com.example.foodx_be.service;

import com.example.foodx_be.dto.AddRestaurantCommand;
import com.example.foodx_be.dto.RestaurantDTO;
import com.example.foodx_be.dto.UpdateRestaurantCommand;
import com.example.foodx_be.enity.Restaurant;
import com.example.foodx_be.enity.Tag;
import com.example.foodx_be.enity.UpdateRestaurant;
import com.example.foodx_be.enity.User;
import com.example.foodx_be.exception.NoResultsFoundException;
import com.example.foodx_be.repository.RestaurantRepository;
import com.example.foodx_be.repository.UpdateRestaurantRepository;
import com.example.foodx_be.ulti.BoundingBoxCalculator;
import com.example.foodx_be.ulti.RestaurantState;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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
    public Page<RestaurantDTO> getNearByRestaurant(BigDecimal longitude, BigDecimal latitude, double radiusInKm, int pageNo, int limit) {
        double[] boundingBox = BoundingBoxCalculator.calculateBoundingBox(latitude.doubleValue(), longitude.doubleValue(), radiusInKm);

        //min lat, min long, max lat, max log
        List<Restaurant> restaurants = restaurantRepository.findRestaurantsWithinBoundingBox(boundingBox[0], boundingBox[1], boundingBox[2], boundingBox[3]);
        if (restaurants.isEmpty()) {
            throw new NoResultsFoundException();
        }
        return converListRestaurantEnityToPage(restaurants, pageNo, limit);
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
                .restaurantState(restaurant.getRestaurantState())
                .timeAdded(restaurant.getTimeAdded())
                .hasAnOwner(restaurant.getHasAnOwner())
                .userAdd(userService.convertTouserBasicInfor(restaurant.getUserAdd()))
                .tagDTOList(tagService.convertToListTagDTO(restaurantTagService.getListTagOfRestaurant(restaurant.getId())));
        if (restaurant.getUserAdd() != null) {
            builder.userAdd(userService.convertTouserBasicInfor(restaurant.getUserAdd()));
        }
        return builder.build();
    }


}
