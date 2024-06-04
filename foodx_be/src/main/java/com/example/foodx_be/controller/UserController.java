package com.example.foodx_be.controller;

import com.example.foodx_be.dto.request.BusinessProofCreationRequest;
import com.example.foodx_be.dto.request.UserUpdateRequest;
import com.example.foodx_be.dto.response.UserResponse;
import com.example.foodx_be.enity.User;
import com.example.foodx_be.exception.APIResponse;
import com.example.foodx_be.repository.UserRepository;
import com.example.foodx_be.service.BusinessProofService;
import com.example.foodx_be.service.FiltersSpecificationImpl;
import com.example.foodx_be.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@AllArgsConstructor
@RestController
@RequestMapping("/users")
public class UserController {
    private UserService userService;
    private BusinessProofService businessProofService;

    private UserRepository userRepository;
    private FiltersSpecificationImpl<User> specification;
    @Operation(
            summary = "Lấy ra thông tin của 1 người dùng",
            responses = {
                    @ApiResponse(
                            description = "Thành công",
                            responseCode = "200"
                    ),
                    @ApiResponse(
                            description = "Không tìm thấy kết quả",
                            responseCode = "404"
                    )
            }

    )
    @GetMapping("/{id}")
    public APIResponse<UserResponse> findById(@PathVariable UUID id) {
        return APIResponse.<UserResponse>builder()
                .result(userService.getUserByID(id))
                .build();
    }

    @Operation(
            summary = "Cập nhật thông tin cá nhân của người dùng",
            responses = {
                    @ApiResponse(
                            description = "Thành công",
                            responseCode = "200"
                    ),
                    @ApiResponse(
                            description = "Không tìm thấy kết quả",
                            responseCode = "404"
                    ),
                    @ApiResponse(
                            description = "Không có quyền truy cập hoặc Token không hợp lệ",
                            responseCode = "403"
                    )
            }

    )
    @PostMapping("/{id}")
    public APIResponse<UserResponse> updateUser(@RequestBody UserUpdateRequest userUpdateRequest) {
        return APIResponse.<UserResponse>builder()
                .result(userService.updateUser(userUpdateRequest))
                .build();
    }


    @Operation(
            summary = "Lấy ra danh sách các người dùng dựa trên tên người dùng",
            responses = {
                    @ApiResponse(
                            description = "Thành công",
                            responseCode = "200"
                    ),
                    @ApiResponse(
                            description = "Không tìm thấy kết quả",
                            responseCode = "404"
                    )
            }

    )
    @GetMapping("/search")
    public APIResponse<Page<UserResponse>> findUsersByName(@RequestParam(name = "name") String name,
                                                              @RequestParam(name = "pageNo", defaultValue = "0") int pageNo,
                                                              @RequestParam(name = "limit", defaultValue = "5") int limit) {
        return APIResponse.<Page<UserResponse>>builder()
                .result(userService.getUsersByName(pageNo, limit, name))
                .build();
    }

    @Operation(
            summary = "Cập nhật ảnh cá nhân của người dùng",
            responses = {
                    @ApiResponse(
                            description = "Thành công",
                            responseCode = "200"
                    ),
                    @ApiResponse(
                            description = "Không tìm thấy kết quả",
                            responseCode = "404"
                    ),
                    @ApiResponse(
                            description = "Không có quyền truy cập hoặc Token không hợp lệ",
                            responseCode = "403"
                    )
            }

    )
    @PostMapping("/avatar/{idUser}")
    public APIResponse<Void> updateUserAvatar(@RequestParam MultipartFile multipartFile,
                                                       @PathVariable UUID idUser) throws IOException {
        userService.updateUserAvatar(idUser, multipartFile);
        return APIResponse.<Void>builder()
                .build();
    }

    @GetMapping("/myInfo")
    public APIResponse<UserResponse> getMyInfo() {
        return APIResponse.<UserResponse>builder()
                .result(userService.getMyInfo())
                .build();
    }


    @Operation(
            summary = "Gừi yêu cầu xác thực nhà hàng",
            responses = {
                    @ApiResponse(
                            description = "Thành công",
                            responseCode = "200"
                    ),
                    @ApiResponse(
                            description = "Không tìm thấy kết quả",
                            responseCode = "404"
                    ),
                    @ApiResponse(
                            description = "Không có quyền truy cập hoặc Token không hợp lệ",
                            responseCode = "403"
                    ),
                    @ApiResponse(
                            description = "Yêu cầu nộp bằng chứng",
                            responseCode = "405"
                    )
            }

    )
    @PostMapping("/claimBusiness/{idRestaurant}")
    public APIResponse<Void> addBusinessProof(@PathVariable UUID idRestaurant,
                                              @RequestPart("data") BusinessProofCreationRequest businessProofCreationRequest,
                                                       @RequestPart(value = "multipartFile") MultipartFile multipartFile) throws IOException {
        businessProofCreationRequest.setIdRestaurant(idRestaurant);
        businessProofService.addBusinessProof(businessProofCreationRequest, multipartFile);
        return APIResponse.<Void>builder()
                .build();
    }


}
