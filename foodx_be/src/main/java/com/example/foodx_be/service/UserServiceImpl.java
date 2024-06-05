package com.example.foodx_be.service;

import com.example.foodx_be.dto.request.UserCreationRequest;
import com.example.foodx_be.dto.request.UserUpdateRequest;
import com.example.foodx_be.dto.response.PageRequestDTO;
import com.example.foodx_be.dto.response.RequestDTO;
import com.example.foodx_be.dto.response.UserBasicInforResponse;
import com.example.foodx_be.dto.response.UserResponse;
import com.example.foodx_be.enity.User;
import com.example.foodx_be.exception.AppException;
import com.example.foodx_be.exception.ErrorCode;
import com.example.foodx_be.mapper.UserMapper;
import com.example.foodx_be.repository.UserRepository;
import com.example.foodx_be.ulti.GlobalOperator;
import com.example.foodx_be.ulti.Role;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService{
    private CloudiaryService cloudiaryService;
    private UserMapper userMapper;
    private FiltersSpecificationImpl<User> specification;

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
    public Page<UserResponse> getUserBySpecification(RequestDTO requestDTO) {
        Specification<User> userSpecification = specification.getSearchSpecification(requestDTO.getSearchRequestDTO(), GlobalOperator.AND);
        userSpecification.and(specification.sortByColumn(requestDTO.getSortByColumn(), requestDTO.getSort()));
        Pageable pageable = new PageRequestDTO().getPageable(requestDTO.getPageRequestDTO());
        Page<User> usersPage = userRepository.findAll(userSpecification, pageable);
        if (usersPage.getContent().isEmpty()) {
            throw new AppException(ErrorCode.USER_EXISTED);
        }
        return usersPage.map(userMapper::toUserResponse);
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
