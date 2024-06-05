package com.example.foodx_be.controller;

import com.example.foodx_be.dto.response.OpenTimeDTO;
import com.example.foodx_be.exception.APIResponse;
import com.example.foodx_be.service.OpenTimeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@Tag(name = "Open Time")
@RequestMapping("/opentimes")
@AllArgsConstructor
public class OpenTimeController {
    private OpenTimeService openTimeService;

    @Operation(
            description = "Lấy ra thông tin về giờ mở cửa của 1 nhà hàng dựa trên ID của nhà hàng đó",
            summary = "Lấy ra thông tin về giờ mở cửa của nhà hàng",
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
    public APIResponse<List<OpenTimeDTO>> getOpenTimeOfRestaurant(@PathVariable UUID idRestaurant) {
        return APIResponse.<List<OpenTimeDTO>>builder()
                .result(openTimeService.getOpenTimeOfRestaurant(idRestaurant))
                .build();
    }


}
