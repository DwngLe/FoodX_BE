package com.example.foodx_be.service;


import com.example.foodx_be.dto.RegisterCommand;
import com.example.foodx_be.dto.UpdateUserComand;
import com.example.foodx_be.dto.UserBasicInfor;
import com.example.foodx_be.dto.UserDTO;
import com.example.foodx_be.enity.User;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

public interface UserService {
    UserDTO saveUser(RegisterCommand registerCommand);

    User getUser(String username);
    User getUser(UUID idUser);

    UserDTO getUserByID(UUID id);

    Page<UserDTO> getUsersByName(int pageNo, int limit, String name);

    UserDTO updateUser(UpdateUserComand updateUserComand);

    void updateUserAvatar(String username, MultipartFile multipartFile) throws IOException;

    UserDTO convertToDTO(User user);

    User convertToUser(RegisterCommand registerCommand);

    User convertToUser(UserDTO userDTO);
    UserBasicInfor convertTouserBasicInfor(User user);

    User convertToUser(UpdateUserComand updateUserComand);
    User updateUserPoint(UUID idUser, int point);
}
