package com.example.foodx_be.service;

import com.example.foodx_be.dto.request.UserCreationRequest;
import com.example.foodx_be.dto.request.UserUpdateRequest;
import com.example.foodx_be.dto.response.UserBasicInforResponse;
import com.example.foodx_be.dto.response.UserResponse;
import com.example.foodx_be.enity.User;
import com.example.foodx_be.exception.AppException;
import com.example.foodx_be.exception.ErrorCode;
import com.example.foodx_be.mapper.UserMapper;
import com.example.foodx_be.repository.UserRepository;
import com.example.foodx_be.ulti.Role;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService{
    private CloudiaryService cloudiaryService;
    private UserMapper userMapper;

    private UserRepository userRepository;
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    private final String FOLDER_UPLOAD = "Avatar";

    @Override
    public void updateUserAvatar(UUID idUser, MultipartFile multipartFile) throws IOException {
        Map result = cloudiaryService.uploadFile(multipartFile, FOLDER_UPLOAD);
        User user = getUser(idUser);
        user.setAvatarLink((String)result.get("url"));
        userRepository.save(user);
    }

    @Override
    public UserResponse saveUser(UserCreationRequest userCreationRequest) {
        if (userRepository.existsByUsername(userCreationRequest.getUsername())) {
            throw new AppException(ErrorCode.USER_EXISTED);
        }
        if (!userCreationRequest.getPassword().equals(userCreationRequest.getRepeatPassword())) {
            throw new AppException(ErrorCode.PASSWORD_MISS_MATCH);
        }
        User user = userMapper.toUser(userCreationRequest);

        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        HashSet<String> roles = new HashSet<>();
        roles.add(Role.USER.name());
        user.setRoles(roles);
        return userMapper.toUserResponse(userRepository.save(user));
    }

    @Override
    public User getUser(String username) {
        Optional<User> user = userRepository.findByUsername(username);
        return unwrapUser(user);
    }

    @Override
    public User getUser(UUID idUser) {
        Optional<User> user = userRepository.findById(idUser);
        return unwrapUser(user);
    }

    @Override
    public UserResponse getUserByID(UUID id) {
        Optional<User> user = userRepository.findById(id);
        return userMapper.toUserResponse(unwrapUser(user));
    }

    @Override
    public Page<UserResponse> getUsersByName(int pageNo, int limit, String name) {
        List<User> userList = userRepository.findByNameContaining(name);
        if(userList.isEmpty()){
            throw new AppException(ErrorCode.USER_NOT_EXISTED);
        }
        List<UserResponse> userResponseList = new ArrayList<>();
        for (User user : userList) {
            userResponseList.add(userMapper.toUserResponse(user));
        }
        Pageable pageable = PageRequest.of(pageNo, limit);

        int startIndex = (int) pageable.getOffset();
        int endIndex = (int) Math.min(pageable.getOffset() + pageable.getPageSize(), userResponseList.size());
        List<UserResponse> subList = userResponseList.subList(startIndex, endIndex);
        return new PageImpl<>(subList, pageable, userResponseList.size());
    }

    @PostAuthorize("returnObject.id.toString() == authentication.principal.claims['sub']") //phai chuyen UUID ve String
    @Override
    public UserResponse updateUser(UserUpdateRequest userUpdateRequest) {
        var context = SecurityContextHolder.getContext();
        UUID idUser = (UUID.fromString(context.getAuthentication().getName()));
        Optional<User> optionalUser = userRepository.findById(idUser);
        User user = unwrapUser(optionalUser);
        userMapper.updateUser(user, userUpdateRequest);
        return userMapper.toUserResponse(userRepository.save(user));
    }


    static User unwrapUser(Optional<User> entity) {
        if (entity.isPresent()) return entity.get();
        else throw new AppException(ErrorCode.USER_NOT_EXISTED);
    }

    @Override
    public UserBasicInforResponse convertTouserBasicInfor(User user) {
        return UserBasicInforResponse.builder()
                .idUser(user.getId())
                .name(user.getName())
                .avatarLink(user.getAvatarLink())
                .points(user.getPoints())
                .build();
    }

    @Override
    public User updateUserPoint(UUID idUser, int point) {
        User user= unwrapUser(userRepository.findById(idUser));
        user.setPoints(user.getPoints() + point);
        userRepository.save(user);
        return user;
    }

    @Override
    public UserResponse getMyInfo() {
        var context = SecurityContextHolder.getContext();
        UUID idUser = UUID.fromString(context.getAuthentication().getName());
        User user = userRepository.findById(idUser).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
        return userMapper.toUserResponse(user);
    }
}
