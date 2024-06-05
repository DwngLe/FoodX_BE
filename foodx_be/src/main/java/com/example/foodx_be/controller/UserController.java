package com.example.foodx_be.controller;

import com.example.foodx_be.dto.request.BusinessProofCreationRequest;
import com.example.foodx_be.dto.request.UserUpdateRequest;
import com.example.foodx_be.dto.response.RequestDTO;
import com.example.foodx_be.dto.response.UserResponse;
import com.example.foodx_be.exception.APIResponse;
import com.example.foodx_be.service.BusinessProofService;
import com.example.foodx_be.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@AllArgsConstructor
@RestController
@Tag(name = "User")
@RequestMapping("/users")
public class UserController {
    private UserService userService;
    private BusinessProofService businessProofService;

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

    @SecurityRequirement(name = "bearAuth")
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
                    ),
                    @ApiResponse(
                            description = "Xác thực không thành công",
                            responseCode = "401"
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
            summary = "Lấy ra danh sách các người dùng dựa trên các tiêu chí",
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
    @PostMapping("/specification")
    public APIResponse<Page<UserResponse>> findUsersBySpecification(@RequestBody RequestDTO requestDTO) {
        return APIResponse.<Page<UserResponse>>builder()
                .result(userService.getUserBySpecification(requestDTO))
                .build();
    }

    @SecurityRequirement(name = "bearAuth")
    @Operation(
            description = "Người dùng gửi lên ảnh để cập nhật. Ảnh sẽ được resize về kích thước 120x120",
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
                    ),
                    @ApiResponse(
                            description = "Xác thực không thành công",
                            responseCode = "401"
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

    @SecurityRequirement(name = "bearAuth")
    @Operation(
            description = "Người dùng xem thông tin cá nhân của họ",
            summary = "Xem thông tin cá nhân",
            responses = {
                    @ApiResponse(
                            description = "Thành công",
                            responseCode = "200"
                    ),
                    @ApiResponse(
                            description = "Thông tin không hợp lệ",
                            responseCode = "405"
                    ),
                    @ApiResponse(
                            description = "Xác thực không thành công",
                            responseCode = "401"
                    )
            }
    )
    @GetMapping("/myInfo")
    public APIResponse<UserResponse> getMyInfo() {
        return APIResponse.<UserResponse>builder()
                .result(userService.getMyInfo())
                .build();
    }

    @SecurityRequirement(name = "bearAuth")
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
                    ),
                    @ApiResponse(
                            description = "Xác thực không thành công",
                            responseCode = "401"
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
