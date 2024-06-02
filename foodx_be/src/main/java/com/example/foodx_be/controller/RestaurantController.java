package com.example.foodx_be.controller;

import com.example.foodx_be.dto.request.AddRestaurantCommand;
import com.example.foodx_be.dto.request.UpdateRestaurantCommand;
import com.example.foodx_be.dto.response.NearbyRequestDTO;
import com.example.foodx_be.dto.response.RequestDTO;
import com.example.foodx_be.dto.response.RestaurantDTO;
import com.example.foodx_be.enity.RestaurantTag;
import com.example.foodx_be.service.RestaurantService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@AllArgsConstructor
@RestController
@RequestMapping("/restaurants")
public class RestaurantController {
    private RestaurantService restaurantService;

    @Operation(
            summary = "Thêm nhà hàng",
            responses = {
                    @ApiResponse(
                            description = "Thành công",
                            responseCode = "200"
                    ),
                    @ApiResponse(
                            description = "Không có quyền truy cập hoặc Token không hợp lệ",
                            responseCode = "403"
                    )
            }
    )
    @PostMapping("")
    public ResponseEntity<HttpStatus> addRestaurant(@RequestBody AddRestaurantCommand addRestaurantCommand) {
        restaurantService.addRestaurant(addRestaurantCommand);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @Operation(
            summary = "Xem thông tin 1 nhà hàng dựa trên ID nhà hàng",
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
    @GetMapping("/{idRestaurant}")
    public ResponseEntity<RestaurantDTO> getRestaurant(@PathVariable UUID idRestaurant) {
        return new ResponseEntity<>(restaurantService.getRestaurantDTO(idRestaurant), HttpStatus.OK);
    }

    @Operation(
            description = "Lấy ra danh sách các nhà hàng áp dụng Filtering, Sorting, Searching và Pageable",
            summary = "Lấy ra danh sách các nhà hàng dựa trên các tiêu chí",
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
    public ResponseEntity<Page<RestaurantDTO>> getRestaurants(@RequestBody RequestDTO requestDTO) {
        return new ResponseEntity<>(restaurantService.getRestaurantBySpecification(requestDTO), HttpStatus.OK);
    }

    @Operation(
            summary = "Lấy ra danh sách các nhà hàng dựa trên Tag",
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
    //for testing purpose
    @PostMapping("/search/tag")
    public ResponseEntity<Page<RestaurantTag>> getRestaurantsByTag(@RequestBody RequestDTO requestDTO) {
        return new ResponseEntity<>(restaurantService.getListRestaurantByTag(requestDTO), HttpStatus.OK);
    }

    @Operation(
            description = "Gửi lên bản cập nhật thông tin của 1 nhà hàng dựa trên ID của nhà hàng đấy. Bản cập nhật có trạng thái mặc định là PENDING",
            summary = "Cập nhật thông tin của 1 nhà hàng dựa trên ID nhà hàng",
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
    @PostMapping("/{idRestaurant}")
    public ResponseEntity<HttpStatus> updateRestaurant(@PathVariable UUID idRestaurant,
                                                       @RequestBody UpdateRestaurantCommand updateRestaurantCommand) {
        restaurantService.updateRestaurant(idRestaurant, updateRestaurantCommand);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Operation(
            description = "Tìm tất cả các nhà hàng dựa trên Longitude, Latitude và bán kính (tính theo Km)",
            summary = "Lấy ra danh sách các nhà hàng xung quanh vị trí người dùng",
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
    @PostMapping("/nearby")
    public ResponseEntity<Page<RestaurantDTO>> getRestaurantNearBy(@RequestBody NearbyRequestDTO nearbyRequestDTO) {
        return new ResponseEntity<>(restaurantService.getNearByRestaurant(nearbyRequestDTO.getRequestDTO(), nearbyRequestDTO.getLocationDTO()), HttpStatus.OK);
    }
}
