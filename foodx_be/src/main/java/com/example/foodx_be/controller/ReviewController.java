package com.example.foodx_be.controller;

import com.example.foodx_be.dto.AddReviewRestaurantCommand;
import com.example.foodx_be.dto.ReviewRestaurantDTO;
import com.example.foodx_be.service.ReviewService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@RestController
@AllArgsConstructor
@RequestMapping("/reviews")
public class ReviewController {
    private ReviewService reviewService;

    @Operation(
            summary = "Thêm đánh giá cho 1 nhà hàng",
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
    @PostMapping("")
    public ResponseEntity<HttpStatus> addReview(@RequestPart("data") AddReviewRestaurantCommand addReviewCommand,
                                                @RequestPart(value = "multipartFiles", required = false) MultipartFile[] multipartFiles) throws IOException {
        reviewService.addReview(addReviewCommand, multipartFiles);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @Operation(
            description = "Lấy thông tin các bài đánh giá của 1 nhà hàng dựa trên ID của nhà hàng đó",
            summary = "Lấy ra danh sách các bài đánh giá của 1 nhà hàng",
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
    public ResponseEntity<Page<ReviewRestaurantDTO>> getListReviewOfRestaurant(@PathVariable UUID idRestaurant,
                                                                               @RequestParam(name = "pageNo", defaultValue = "0") int pageNo,
                                                                               @RequestParam(name = "limit", defaultValue = "5") int limit) {
        return new ResponseEntity<>(reviewService.getListReviewOfRestaurant(pageNo, limit, idRestaurant), HttpStatus.OK);
    }

    @Operation(
            description = "Lấy ra các bài đánh giá gần đây của tất cả các nhà hàng. Truyền vào Limit và pageNo để giới hạn",
            summary = "Lấy ra danh sách các bài đánh giá gần đây",
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
    @GetMapping("/recent")
    public ResponseEntity<List<ReviewRestaurantDTO>> getListRecentReview(@RequestParam(name = "pageNo", defaultValue = "0") int pageNo,
                                                                         @RequestParam(name = "limit", defaultValue = "5") int limit) {
        return new ResponseEntity<>(reviewService.getListRecentReview(pageNo, limit), HttpStatus.OK);

    }
}
