package com.example.foodx_be.service;

import java.io.IOException;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import com.example.foodx_be.dto.request.UserCreationRequest;
import com.example.foodx_be.dto.request.UserUpdateRequest;
import com.example.foodx_be.dto.response.RequestDTO;
import com.example.foodx_be.dto.response.UserBasicInforResponse;
import com.example.foodx_be.dto.response.UserResponse;
import com.example.foodx_be.enity.User;

public interface UserService {
    UserResponse saveUser(UserCreationRequest userCreationRequest);

    User getUser(UUID idUser);

    UserResponse getUserByID(UUID id);

    Page<UserResponse> getUserBySpecification(RequestDTO requestDTO);

    UserResponse updateUser(UserUpdateRequest userUpdateRequest);

    void updateUserAvatar(UUID idUser, MultipartFile multipartFile) throws IOException;

    UserBasicInforResponse convertTouserBasicInfor(User user);

    User updateUserPoint(UUID idUser, int point);

    UserResponse getMyInfo();
}
