package com.example.foodx_be.controller;

import com.example.foodx_be.dto.response.TagDTO;
import com.example.foodx_be.exception.APIResponse;
import com.example.foodx_be.service.TagService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@AllArgsConstructor
@RestController
@Tag(name = "Tag")
@RequestMapping("/tags")
public class TagController {
    private TagService tagService;

    @Operation(
            summary = "Lấy ra danh sách các thẻ Tag",
            responses = {
                    @ApiResponse(description = "Thành công", responseCode = "200"),
                    @ApiResponse(description = "Không tìm thấy kết quả", responseCode = "404")
            })
    @GetMapping("")
    public APIResponse<List<TagDTO>> getListTag() {
        return APIResponse.<List<TagDTO>>builder()
                .result(tagService.getListTag())
                .build();
    }
}
