package com.example.foodx_be.controller;

import com.example.foodx_be.dto.response.BusinessProofDTO;
import com.example.foodx_be.dto.response.RestaurantDTO;
import com.example.foodx_be.dto.response.RestaurantUpdateDTO;
import com.example.foodx_be.dto.response.TagDTO;
import com.example.foodx_be.enums.RestaurantState;
import com.example.foodx_be.enums.UpdateState;
import com.example.foodx_be.exception.APIResponse;
import com.example.foodx_be.service.AdminService;
import com.example.foodx_be.service.BusinessProofService;
import com.example.foodx_be.service.RestaurantService;
import com.example.foodx_be.service.TagService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@AllArgsConstructor
@SecurityRequirement(name = "bearAuth")
@RequestMapping("/admin")
@Tag(name = "Admin")
public class AdminController {
    private RestaurantService restaurantService;
    private AdminService adminService;
    private BusinessProofService businessProofService;
    private TagService tagService;

    @Operation(
            description = "Xem danh sách các bản cập nhật của nhà hàng dựa trên trạng thái cập nhật (PENDING, ACCEPTED, DENIED)",
            summary = "Xem danh sách các bản cập nhật nhà hàng của người dùng gửi lên",
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
    @GetMapping("/reviewUpdates")
    public APIResponse<Page<RestaurantUpdateDTO>> getUpdateRestaurantList(@RequestParam UpdateState updateState,
                                                                             @RequestParam int pageNo,
                                                                             @RequestParam int limit) {
        return APIResponse.<Page<RestaurantUpdateDTO>>builder()
                .result(adminService.getRestaurantUpdateList(pageNo, limit, updateState))
                .build();
    }

    @Operation(
            description = "Duyệt, huỷ các bản cập nhật nhà hàng bằng cách truyền lên UpdateState (DENIED, ACCPECTED)",
            summary = "Duyệt, huỷ các bản cập nhật nhà hàng",
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
    @PostMapping("/reviewUpdates/{idUpdate}")
    public APIResponse<Void> reviewRestaurantUpdate(@PathVariable UUID idUpdate,
                                                    @RequestParam UpdateState updateState) {
        adminService.reviewRestaurantUpdate(idUpdate, updateState);
        return APIResponse.<Void>builder().build();
    }

    @Operation(
            description = "Thay đổi trạng thái của 1 nhà hàng bằng cách truyền lên RestaurantState (PENDING, ACTIVE, CLOSED, DENIED, BANNED)",
            summary = "Cập nhật trạng thái của 1 nhà hàng",
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
    @PostMapping("/restaurants/{idRestaurant}/updateState")
    public APIResponse<Void> reviewRestaurantUpdate(@PathVariable UUID idRestaurant,
                                                             @RequestParam RestaurantState restaurantState) {
        adminService.reviewRestaurantState(idRestaurant, restaurantState);
        return APIResponse.<Void>builder().build();
    }

    @Operation(
            description = "Xem danh sách các nhà hàng dựa trên trạng thái của nhà hàng (PENDING, ACTIVE, CLOSED, DENIED, BANNED)",
            summary = "Xem danh sách các nhà hàng",
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
    @GetMapping("/restaurants")
    public APIResponse<Page<RestaurantDTO>> getRestaurantByRestaurantState(@RequestParam RestaurantState restaurantState,
                                                                              @RequestParam(defaultValue = "0") int pageNo,
                                                                              @RequestParam(defaultValue = "10") int limit) {
        return APIResponse.<Page<RestaurantDTO>>builder()
                .result(restaurantService.getRestaurantByRestaurantState(pageNo, limit, restaurantState))
                .build();
    }

    @Operation(
            description = "Xem danh sách các bằng chứng dựa trên trạng thái của bằng chứng (PENDING, DENIED, ACCEPTED)",
            summary = "Xem danh sách các bằng chứng chứng minh có liên quan đến nhà hàng",
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
    @GetMapping("/businessProofs")
    public APIResponse<Page<BusinessProofDTO>> getListBusinessProofByState(@RequestParam UpdateState state,
                                                                              @RequestParam int pageNo,
                                                                              @RequestParam int limit) {
        return APIResponse.<Page<BusinessProofDTO>>builder()
                .result(businessProofService.getListBusinessProofByState(pageNo, limit, state))
                .build();
    }

    @Operation(
            description = "Xem chi tiết 1 bằng chứng dựa trên ID của bằng chứng",
            summary = "Xem chi tiết 1 bằng chứng",
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
    @GetMapping("/businessProofs/{idBusinessProof}")
    public APIResponse<BusinessProofDTO> getBusinessProof(@PathVariable UUID idBusinessProof) {
        return APIResponse.<BusinessProofDTO>builder()
                .result(businessProofService.getBusinessProof(idBusinessProof))
                .build();
    }

    @Operation(
            description = "Duyệt, từ chối 1 bằng chứng bằng cách truyền lên UpdateState (PENDING, DENIED, ACCEPTED)",
            summary = "Duyệt, từ chối bằng chứng",
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
    @PostMapping("/businessProofs/{idBusinessProof}")
    public APIResponse<Void> reviewBusinessProof(@PathVariable UUID idBusinessProof,
                                                          @RequestParam UpdateState updateState) {
        businessProofService.reviewBusinessProof(idBusinessProof, updateState);
        return APIResponse.<Void>builder().build();
    }

    @Operation(
            description = "Thêm danh sách các thẻ tag: Tag name và Tag Description",
            summary = "Thêm danh sách thẻ tag",
            responses = {
                    @ApiResponse(
                            description = "Thành công",
                            responseCode = "200"
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
    @PostMapping("/tags")
    public APIResponse<Void> addTag(@RequestBody List<TagDTO> tagDTOList) {
        tagService.addTag(tagDTOList);
        return APIResponse.<Void>builder().build();
    }


}
