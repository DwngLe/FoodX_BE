package com.example.foodx_be.service;

import com.example.foodx_be.dto.RegisterCommand;
import com.example.foodx_be.dto.UpdateUserComand;
import com.example.foodx_be.dto.UserDTO;
import com.example.foodx_be.enity.User;
import com.example.foodx_be.exception.UserExistedException;
import com.example.foodx_be.exception.UserNotFoundException;
import com.example.foodx_be.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;


import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService{
    private UserRepository userRepository;
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    @Override
    public User saveUser(RegisterCommand registerCommand) {

        Optional<User> userRegister = userRepository.findByUsername(registerCommand.getUsername());
        if(userRegister.isPresent()){
            throw new UserExistedException(registerCommand.getUsername());
        }
        User user = convertToUser(registerCommand);
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        userRepository.save(user);
        return user;
    }

    @Override
    public User getUser(String username) {
        Optional<User> user = userRepository.findByUsername(username);
        return unwrapUser(user);
    }

    @Override
    public UserDTO getUserByID(UUID id) {
        Optional<User> user = userRepository.findById(id);
        return convertToDTO(unwrapUser(user));
    }

    @Override
    public Page<UserDTO> getUsersByName(int pageNo, int limit, String name) {
        List<User> userList = userRepository.findByNameContaining(name);
        if(userList.isEmpty()){
            throw new UserNotFoundException();
        }
        List<UserDTO> userDTOList = new ArrayList<>();
        for (User user : userList) {
            userDTOList.add(convertToDTO(user));
        }
        Pageable pageable = PageRequest.of(pageNo, limit);

        int startIndex = (int) pageable.getOffset();
        int endIndex = (int)Math.min(pageable.getOffset() + pageable.getPageSize(), userDTOList.size());
        List<UserDTO> subList = userDTOList.subList(startIndex, endIndex);
        return new PageImpl<>(subList, pageable, userDTOList.size());
    }

    @Override
    public UserDTO updateUser(UpdateUserComand updateUserComand) {
        Optional<User> optionalUser = userRepository.findById(updateUserComand.getId());
        User userOld = unwrapUser(optionalUser);
        User userNew = convertToUser(updateUserComand);

        userOld.setName(userNew.getName());
        userOld.setPhoneNumber(userNew.getPhoneNumber());
        userOld.setEmail(userNew.getEmail());
        userOld.setAvatarLink(userNew.getAvatarLink());
        userOld.setFacebookLink(userNew.getFacebookLink());
        userOld.setInstagramLink(userNew.getInstagramLink());
        userOld.setWard(userNew.getWard());
        userOld.setDistrict(userNew.getDistrict());
        userOld.setCity(userNew.getCity());

        userRepository.save(userOld);
        return convertToDTO(userOld);
    }


    static User unwrapUser(Optional<User> entity) {
        if (entity.isPresent()) return entity.get();
        else throw new UserNotFoundException();
    }

    @Override
    public User convertToUser(RegisterCommand userDTO) {
        return User.builder()
                .username(userDTO.getUsername())
                .password(userDTO.getPassword())
                .name(userDTO.getName())
                .phoneNumber(userDTO.getPhoneNumber())
                .email(userDTO.getEmail())
                .ward(userDTO.getWard())
                .district(userDTO.getDistrict())
                .city(userDTO.getCity())
                .build();
    }

    @Override
    public User convertToUser(UserDTO userDTO) {
        return User.builder()
                .name(userDTO.getName())
                .phoneNumber(userDTO.getPhoneNumber())
                .email(userDTO.getEmail())
                .avatarLink(userDTO.getAvatarLink())
                .facebookLink(userDTO.getFacebookLink())
                .instagramLink(userDTO.getInstagramLink())
                .ward(userDTO.getWard())
                .district(userDTO.getDistrict())
                .city(userDTO.getCity())
                .build();
    }

    public User convertToUser(UpdateUserComand updateUserComand) {
        return User.builder()
                .name(updateUserComand.getName())
                .phoneNumber(updateUserComand.getPhoneNumber())
                .email(updateUserComand.getEmail())
                .avatarLink(updateUserComand.getAvatarLink())
                .facebookLink(updateUserComand.getFacebookLink())
                .instagramLink(updateUserComand.getInstagramLink())
                .ward(updateUserComand.getWard())
                .district(updateUserComand.getDistrict())
                .city(updateUserComand.getCity())
                .build();
    }
    @Override
    public UserDTO convertToDTO(User user) {
        return UserDTO.builder()
                .id(user.getId())
                .name(user.getName())
                .phoneNumber(user.getPhoneNumber())
                .email(user.getEmail())
                .avatarLink(user.getAvatarLink())
                .facebookLink(user.getFacebookLink())
                .instagramLink(user.getInstagramLink())
                .ward(user.getWard())
                .district(user.getDistrict())
                .city(user.getCity())
                .jointDate(user.getJointDate())
                .points(user.getPoints())
                .build();
    }

}
