package com.example.foodx_be.controller;

import com.example.foodx_be.dto.AddRestaurantCommand;
import com.example.foodx_be.dto.RestaurantDTO;
import com.example.foodx_be.dto.UpdateRestaurantCommand;
import com.example.foodx_be.service.RestaurantService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
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
            summary = "Lấy ra danh sách các nhà hàng dựa trên keyword",
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
    public ResponseEntity<Page<RestaurantDTO>> getRestaurants(@RequestParam(name = "searchBy") String searchBy,
                                                              @RequestParam(name = "keyword") String keyword,
                                                              @RequestParam(name = "pageNo", defaultValue = "0") int pageNo,
                                                              @RequestParam(name = "limit", defaultValue = "10") int limit) {
        return new ResponseEntity<>(restaurantService.getRestaurantsByKeyword(pageNo, limit, keyword, searchBy), HttpStatus.OK);
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
    @GetMapping("/search/tag")
    public ResponseEntity<Page<RestaurantDTO>> getRestaurants(@RequestParam UUID idTag,
                                                              @RequestParam(name = "pageNo", defaultValue = "0") int pageNo,
                                                              @RequestParam(name = "limit", defaultValue = "10") int limit) {
        return new ResponseEntity<>(restaurantService.getListRestaurantByTag(pageNo, limit, idTag), HttpStatus.OK);
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
    @GetMapping("/nearby")
    public ResponseEntity<Page<RestaurantDTO>> getRestaurantNearBy(@RequestParam BigDecimal longitude,
                                                                   @RequestParam BigDecimal latitude,
                                                                   @RequestParam(defaultValue = "1") double radiusInKm,
                                                                   @RequestParam(name = "pageNo", defaultValue = "0") int pageNo,
                                                                   @RequestParam(name = "limit", defaultValue = "10") int limit) {
        return new ResponseEntity<>(restaurantService.getNearByRestaurant(longitude, latitude, radiusInKm, pageNo, limit), HttpStatus.OK);
    }
}
