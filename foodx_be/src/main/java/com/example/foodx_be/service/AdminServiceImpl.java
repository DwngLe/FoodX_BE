package com.example.foodx_be.service;

import com.example.foodx_be.dto.*;
import com.example.foodx_be.enity.OpenTime;
import com.example.foodx_be.enity.Restaurant;
import com.example.foodx_be.enity.UpdateOpenTime;
import com.example.foodx_be.enity.UpdateRestaurant;
import com.example.foodx_be.exception.NoResultsFoundException;
import com.example.foodx_be.repository.RestaurantRepository;
import com.example.foodx_be.repository.UpdateRestaurantRepository;
import com.example.foodx_be.ulti.RestaurantState;
import com.example.foodx_be.ulti.UpdateState;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
public class AdminServiceImpl implements AdminService {
    private RestaurantService restaurantService;
    private UserService userService;
    private UpdateRestaurantRepository updateRestaurantRepository;
    private RestaurantRepository restaurantRepository;

    @Override
    public void reviewRestaurantState(UUID idRestaurant, ReviewRestaurantState restaurantState) {
        Restaurant restaurantAdd = restaurantService.getRestaurantEnity(idRestaurant);
        restaurantAdd.setRestaurantState(restaurantState.getRestaurantState());
        restaurantRepository.save(restaurantAdd);
    }

    @Override
    public Page<RestaurantUpdateDTO> getRestaurantUpdateList(int pageNo, int limit, UpdateState updateState) {
        List<UpdateRestaurant> updateRestaurantList = updateRestaurantRepository.findAllByUpdateState(updateState);
        if (updateRestaurantList.isEmpty()) {
            throw new NoResultsFoundException();
        }
        List<RestaurantUpdateDTO> restaurantUpdateDTOList = new ArrayList<>();
        for (UpdateRestaurant updateRestaurant : updateRestaurantList) {
            restaurantUpdateDTOList.add(convertToRestaurantUpdateDTO(updateRestaurant));
        }

        Pageable pageable = PageRequest.of(pageNo, limit);
        int startIndex = (int) pageable.getOffset();
        int endIndex = (int) Math.min(pageable.getOffset() + pageable.getPageSize(), restaurantUpdateDTOList.size());
        List<RestaurantUpdateDTO> subList = restaurantUpdateDTOList.subList(startIndex, endIndex);
        return new PageImpl<>(subList, pageable, restaurantUpdateDTOList.size());
    }



    @Override
    public void reviewRestaurantUpdate(UUID idRestaurantUpdate, ReviewUpdate reviewUpdate) {
        UpdateRestaurant updateRestaurant = unwrarpRestaurant(updateRestaurantRepository.findById(idRestaurantUpdate));
        if (reviewUpdate.getUpdateState() == UpdateState.ACCEPTED) {
            try {
                Restaurant restaurant = updateRestaurant.getRestaurant();
                updateFromRestaurantUpdate(updateRestaurant, restaurant);
                restaurantRepository.save(restaurant);
            } catch (Exception e) {
                throw new NoResultsFoundException();
            }
        }
        updateRestaurant.setUpdateState(reviewUpdate.getUpdateState());
        updateRestaurant.setReviewUpdateTime(LocalDateTime.now());
        updateRestaurantRepository.save(updateRestaurant);
    }

    static UpdateRestaurant unwrarpRestaurant(Optional<UpdateRestaurant> entity) {
        if (entity.isPresent()) return entity.get();
        else throw new NoResultsFoundException();
    }


    private RestaurantUpdateDTO convertToRestaurantUpdateDTO(UpdateRestaurant updateRestaurant) {
        RestaurantUpdateDTO.RestaurantUpdateDTOBuilder builder = RestaurantUpdateDTO.builder()
                .idUpdate(updateRestaurant.getId())
                .restaurantName(updateRestaurant.getRestaurantName())
                .houseNumber(updateRestaurant.getHouseNumber())
                .ward(updateRestaurant.getWard())
                .district(updateRestaurant.getDistrict())
                .city(updateRestaurant.getCity())
                .longitude(updateRestaurant.getLongitude())
                .latitude(updateRestaurant.getLatitude())
                .description(updateRestaurant.getDescription())
                .phoneNumber(updateRestaurant.getPhoneNumber())
                .email(updateRestaurant.getEmail())
                .website(updateRestaurant.getWebsite())
                .facebookLink(updateRestaurant.getFacebookLink())
                .instagramLink(updateRestaurant.getInstagramLink())
                .restaurantState(updateRestaurant.getRestaurantState())
                .timeAdded(updateRestaurant.getRestaurant().getTimeAdded())
                .updateState(updateRestaurant.getUpdateState())
                .updateTime(updateRestaurant.getUpdateTime());
        if (updateRestaurant.getRestaurant().getUserOwner() != null) {
            builder.userNameOwner(updateRestaurant.getRestaurant().getUserOwner().getUsername());
        }
        if (updateRestaurant.getUserUpdate() != null) {
            builder.userNameUpdate(updateRestaurant.getUserUpdate().getUsername());
        }
        return builder.build();
    }

    //DARK SIDE ;))


    public void updateFromRestaurantUpdate(UpdateRestaurant restaurantUpdate, Restaurant restaurant) {
        Optional.ofNullable(restaurantUpdate.getRestaurantName()).ifPresent(restaurant::setRestaurantName);
        Optional.ofNullable(restaurantUpdate.getHouseNumber()).ifPresent(restaurant::setHouseNumber);
        Optional.ofNullable(restaurantUpdate.getWard()).ifPresent(restaurant::setWard);
        Optional.ofNullable(restaurantUpdate.getDistrict()).ifPresent(restaurant::setDistrict);
        Optional.ofNullable(restaurantUpdate.getCity()).ifPresent(restaurant::setCity);
        Optional.ofNullable(restaurantUpdate.getLongitude()).ifPresent(restaurant::setLongitude);
        Optional.ofNullable(restaurantUpdate.getLatitude()).ifPresent(restaurant::setLatitude);
        Optional.ofNullable(restaurantUpdate.getDescription()).ifPresent(restaurant::setDescription);
        Optional.ofNullable(restaurantUpdate.getPhoneNumber()).ifPresent(restaurant::setPhoneNumber);
        Optional.ofNullable(restaurantUpdate.getEmail()).ifPresent(restaurant::setEmail);
        Optional.ofNullable(restaurantUpdate.getWebsite()).ifPresent(restaurant::setWebsite);
        Optional.ofNullable(restaurantUpdate.getFacebookLink()).ifPresent(restaurant::setFacebookLink);
        Optional.ofNullable(restaurantUpdate.getInstagramLink()).ifPresent(restaurant::setInstagramLink);
        Optional.ofNullable(restaurantUpdate.getRestaurantState()).ifPresent(restaurant::setRestaurantState);
        List<OpenTime> convertedOpenTimeList = getOpenTimeList(restaurantUpdate);
        restaurant.setOpenTimeList(convertedOpenTimeList);
    }

    private static List<OpenTime> getOpenTimeList(UpdateRestaurant restaurantUpdate) {
        List<OpenTime> convertedOpenTimeList = new ArrayList<>();
        if (restaurantUpdate.getOpenTimeList() != null) {
            for (UpdateOpenTime updateOpenTime : restaurantUpdate.getOpenTimeList()) {
                OpenTime openTime = new OpenTime();
                openTime.setDayOfWeek((updateOpenTime.getDayOfWeek()));
                openTime.setOpeningTime(updateOpenTime.getOpeningTime());
                openTime.setClosingTime(updateOpenTime.getClosingTime());
                convertedOpenTimeList.add(openTime);
            }
        }
        return convertedOpenTimeList;
    }


}

