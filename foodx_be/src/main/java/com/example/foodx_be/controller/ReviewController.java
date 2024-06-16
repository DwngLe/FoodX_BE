package com.example.foodx_be.controller;

import com.example.foodx_be.dto.request.Request;
import com.example.foodx_be.dto.request.ReviewRestaurantCreationRequest;
import com.example.foodx_be.dto.response.ReviewRestaurantDTO;
import com.example.foodx_be.exception.APIResponse;
import com.example.foodx_be.service.ReviewService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@AllArgsConstructor
@Tag(name = "Review")
@RequestMapping("/reviews")
public class ReviewController {
    private ReviewService reviewService;

    @SecurityRequirement(name = "bearAuth")
    @Operation(
            description = "Người dùng thực hiện việc viết review cho nhà hàng, có thể không cần gửi ảnh",
            summary = "Thêm đánh giá cho 1 nhà hàng",
            responses = {
                    @ApiResponse(description = "Thành công", responseCode = "200"),
                    @ApiResponse(description = "Không tìm thấy kết quả", responseCode = "404"),
                    @ApiResponse(description = "Không có quyền truy cập hoặc Token không hợp lệ", responseCode = "403")
            })
    @PostMapping("")
    public APIResponse<Void> addReview(
            @RequestPart("data") ReviewRestaurantCreationRequest addReviewCommand,
            @RequestPart(value = "multipartFiles", required = false) MultipartFile[] multipartFiles)
            throws IOException {
        reviewService.addReview(addReviewCommand, multipartFiles);
        return APIResponse.<Void>builder().build();
    }

    @Operation(
            description = "Lấy thông tin các bài đánh giá của 1 nhà hàng hoặc 1 người dùng dựa vào id",
            summary = "Lấy ra danh sách các bài đánh giá của 1 nhà hàng hoặc 1 người dùng",
            responses = {
                    @ApiResponse(description = "Thành công", responseCode = "200"),
                    @ApiResponse(description = "Không tìm thấy kết quả", responseCode = "404")
            })
    @PostMapping("/specification")
    public APIResponse<Page<ReviewRestaurantDTO>> getListReview(@RequestBody Request request) {
        return APIResponse.<Page<ReviewRestaurantDTO>>builder()
                .result(reviewService.getListReviewBySpecification(request))
                .build();
    }

    @Operation(
            description =
                    "Lấy ra các bài đánh giá gần đây của tất cả các nhà hàng. Truyền vào Limit và pageNo để giới hạn",
            summary = "Lấy ra danh sách các bài đánh giá gần đây",
            responses = {
                    @ApiResponse(description = "Thành công", responseCode = "200"),
                    @ApiResponse(description = "Không tìm thấy kết quả", responseCode = "404")
            })
    @GetMapping("/recent")
    public APIResponse<List<ReviewRestaurantDTO>> getListRecentReview(
            @RequestParam(name = "pageNo", defaultValue = "0") int pageNo,
            @RequestParam(name = "limit", defaultValue = "5") int limit) {
        return APIResponse.<List<ReviewRestaurantDTO>>builder()
                .result(reviewService.getListRecentReview(pageNo, limit))
                .build();
    }
}
