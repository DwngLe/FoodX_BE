package com.example.foodx_be.controller;

import com.example.foodx_be.dto.BusinessProofDTO;
import com.example.foodx_be.dto.RestaurantDTO;
import com.example.foodx_be.dto.RestaurantUpdateDTO;
import com.example.foodx_be.dto.TagDTO;
import com.example.foodx_be.service.AdminService;
import com.example.foodx_be.service.BusinessProofService;
import com.example.foodx_be.service.RestaurantService;
import com.example.foodx_be.service.TagService;
import com.example.foodx_be.ulti.RestaurantState;
import com.example.foodx_be.ulti.UpdateState;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@AllArgsConstructor
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
                    )
            }

    )
    @GetMapping("/reviewUpdates")
    public ResponseEntity<Page<RestaurantUpdateDTO>> getUpdateRestaurantList(@RequestParam UpdateState updateState,
                                                                             @RequestParam int pageNo,
                                                                             @RequestParam int limit) {
        return new ResponseEntity<>(adminService.getRestaurantUpdateList(pageNo, limit, updateState), HttpStatus.OK);
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
                    )
            }

    )
    @PostMapping("/reviewUpdates/{idUpdate}")
    public ResponseEntity<HttpStatus> reviewRestaurantUpdate(@PathVariable UUID idUpdate,
                                                             @RequestParam UpdateState updateState) {
        adminService.reviewRestaurantUpdate(idUpdate, updateState);
        return new ResponseEntity<>(HttpStatus.OK);
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
                    )
            }

    )
    @PostMapping("/restaurants/{idRestaurant}/updateState")
    public ResponseEntity<HttpStatus> reviewRestaurantUpdate(@PathVariable UUID idRestaurant,
                                                             @RequestParam RestaurantState restaurantState) {
        adminService.reviewRestaurantState(idRestaurant, restaurantState);
        return new ResponseEntity<>(HttpStatus.OK);
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
                    )
            }

    )
    @GetMapping("/restaurants")
    public ResponseEntity<Page<RestaurantDTO>> getRestaurantByRestaurantState(@RequestParam RestaurantState restaurantState,
                                                                              @RequestParam(defaultValue = "0") int pageNo,
                                                                              @RequestParam(defaultValue = "10") int limit) {
        return new ResponseEntity<>(restaurantService.getRestaurantByRestaurantState(pageNo, limit, restaurantState), HttpStatus.OK);
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
                    )
            }

    )
    @GetMapping("/businessProofs")
    public ResponseEntity<Page<BusinessProofDTO>> getListBusinessProofByState(@RequestParam UpdateState state,
                                                                              @RequestParam int pageNo,
                                                                              @RequestParam int limit) {
        return new ResponseEntity<>(businessProofService.getListBusinessProofByState(pageNo, limit, state), HttpStatus.OK);
    }

    @Operation(
            description = "Xem chi tiết 1 bằng chứng dựa trên ID",
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
                    )
            }

    )
    @GetMapping("/businessProofs/{idBusinessProof}")
    public ResponseEntity<BusinessProofDTO> getBusinessProof(@PathVariable UUID idBusinessProof) {
        return new ResponseEntity<>(businessProofService.getBusinessProof(idBusinessProof), HttpStatus.OK);
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
                    )
            }

    )
    @PostMapping("/businessProofs/{idBusinessProof}")
    public ResponseEntity<HttpStatus> reviewBusinessProof(@PathVariable UUID idBusinessProof,
                                                          @RequestParam UpdateState updateState) {
        businessProofService.reviewBusinessProof(idBusinessProof, updateState);
        return new ResponseEntity<>(HttpStatus.OK);
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
                    )
            }

    )
    @PostMapping("/tags")
    public ResponseEntity<HttpStatus> addTag(@RequestBody List<TagDTO> tagDTOList) {
        tagService.addTag(tagDTOList);
        return new ResponseEntity<>(HttpStatus.OK);
    }


}
