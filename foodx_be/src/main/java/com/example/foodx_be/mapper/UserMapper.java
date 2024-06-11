package com.example.foodx_be.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import com.example.foodx_be.dto.request.UserCreationRequest;
import com.example.foodx_be.dto.request.UserUpdateRequest;
import com.example.foodx_be.dto.response.UserBasicInforResponse;
import com.example.foodx_be.dto.response.UserResponse;
import com.example.foodx_be.enity.User;

@Mapper(componentModel = "spring")
public interface UserMapper {
    User toUser(UserResponse request);

    User toUser(UserCreationRequest userCreationRequest);

    void updateUser(@MappingTarget User user, UserUpdateRequest userUpdateRequest);

    UserResponse toUserResponse(User user);

    UserBasicInforResponse toUserBasicInfoResponse(User user);
}
