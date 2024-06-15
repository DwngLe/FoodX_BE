package com.example.foodx_be.controller;

import com.example.foodx_be.dto.request.Request;
import com.example.foodx_be.dto.request.RestaurantCreationRequest;
import com.example.foodx_be.dto.request.RestaurantUpdateRequest;
import com.example.foodx_be.dto.response.NearbyRequestDTO;
import com.example.foodx_be.dto.response.RestaurantBasicInfoResponse;
import com.example.foodx_be.dto.response.RestaurantResponse;
import com.example.foodx_be.enity.RestaurantTag;
import com.example.foodx_be.exception.APIResponse;
import com.example.foodx_be.service.RestaurantService;
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
@Tag(name = "Restaurant")
@RequestMapping("/restaurants")
public class RestaurantController {

    private RestaurantService restaurantService;

    @SecurityRequirement(name = "bearAuth")
    @Operation(
            description = "Người dùng gửi lên các thông tin về nhà hàng",
            summary = "Thêm nhà hàng",
            responses = {
                    @ApiResponse(description = "Thành công", responseCode = "200"),
                    @ApiResponse(description = "Không có quyền truy cập hoặc Token không hợp lệ", responseCode = "403"),
                    @ApiResponse(description = "Xác thực không thành công", responseCode = "401")
            })
    @PostMapping("")
    public APIResponse<Void> addRestaurant(@RequestPart("data") RestaurantCreationRequest restaurantCreationRequest,
                                           @RequestPart(value = "multipartFiles", required = true) MultipartFile[] multipartFiles)
            throws IOException {
        restaurantService.addRestaurant(restaurantCreationRequest, multipartFiles);
        return APIResponse.<Void>builder().build();
    }

    @Operation(
            summary = "Xem thông tin 1 nhà hàng dựa trên ID nhà hàng",
            responses = {
                    @ApiResponse(description = "Thành công", responseCode = "200"),
                    @ApiResponse(description = "Không tìm thấy kết quả", responseCode = "404")
            })
    @GetMapping("/{idRestaurant}")
    public APIResponse<RestaurantResponse> getRestaurant(@PathVariable UUID idRestaurant) {
        return APIResponse.<RestaurantResponse>builder()
                .result(restaurantService.getRestaurantDTO(idRestaurant))
                .build();
    }

    @Operation(
            description = "Lấy ra danh sách các nhà hàng áp dụng Filtering, Sorting, Searching và Pageable",
            summary = "Lấy ra danh sách các nhà hàng dựa trên các tiêu chí",
            responses = {
                    @ApiResponse(description = "Thành công", responseCode = "200"),
                    @ApiResponse(description = "Không tìm thấy kết quả", responseCode = "404")
            })
    @PostMapping("/specification")
    public APIResponse<Page<RestaurantBasicInfoResponse>> getRestaurants(@RequestBody Request request) {
        return APIResponse.<Page<RestaurantBasicInfoResponse>>builder()
                .result(restaurantService.getRestaurantBySpecification(request))
                .build();
    }

    @Operation(
            summary = "Lấy ra danh sách các nhà hàng dựa trên Tag",
            responses = {
                    @ApiResponse(description = "Thành công", responseCode = "200"),
                    @ApiResponse(description = "Không tìm thấy kết quả", responseCode = "404")
            })
    @PostMapping("/search/tag")
    public APIResponse<Page<RestaurantTag>> getRestaurantsByTag(@RequestBody Request request) {
        return APIResponse.<Page<RestaurantTag>>builder()
                .result(restaurantService.getListRestaurantByTag(request))
                .build();
    }

    @SecurityRequirement(name = "bearAuth")
    @Operation(
            description =
                    "Gửi lên bản cập nhật thông tin của 1 nhà hàng dựa trên ID của nhà hàng đấy. Bản cập nhật có trạng thái mặc định là PENDING",
            summary = "Cập nhật thông tin của 1 nhà hàng dựa trên ID nhà hàng",
            responses = {
                    @ApiResponse(description = "Thành công", responseCode = "200"),
                    @ApiResponse(description = "Không tìm thấy kết quả", responseCode = "404"),
                    @ApiResponse(description = "Không có quyền truy cập hoặc Token không hợp lệ", responseCode = "403"),
                    @ApiResponse(description = "Xác thực không thành công", responseCode = "401")
            })
    @PostMapping("/{idRestaurant}")
    public APIResponse<Void> updateRestaurant(
            @PathVariable UUID idRestaurant, @RequestBody RestaurantUpdateRequest restaurantUpdateRequest) {
        restaurantService.updateRestaurant(idRestaurant, restaurantUpdateRequest);
        return APIResponse.<Void>builder().build();
    }

    @Operation(
            description = "Tìm tất cả các nhà hàng dựa trên Longitude, Latitude và bán kính (tính theo Km)",
            summary = "Lấy ra danh sách các nhà hàng xung quanh vị trí người dùng",
            responses = {
                    @ApiResponse(description = "Thành công", responseCode = "200"),
                    @ApiResponse(description = "Không tìm thấy kết quả", responseCode = "404")
            })
    @PostMapping("/nearby")
    public APIResponse<Page<RestaurantBasicInfoResponse>> getRestaurantNearBy(@RequestBody NearbyRequestDTO nearbyRequestDTO) {
        return APIResponse.<Page<RestaurantBasicInfoResponse>>builder()
                .result(restaurantService.getNearByRestaurant(
                        nearbyRequestDTO.getRequest(), nearbyRequestDTO.getLocationRequest()))
                .build();
    }
}
