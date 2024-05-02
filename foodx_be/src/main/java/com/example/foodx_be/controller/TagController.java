package com.example.foodx_be.controller;

import com.example.foodx_be.dto.TagDTO;
import com.example.foodx_be.service.TagService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/tags")
public class TagController {
    private TagService tagService;

    @Operation(
            summary = "Lấy ra danh sách các thẻ Tag",
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
    @GetMapping("")
    public ResponseEntity<List<TagDTO>> getListTag(){
        return new ResponseEntity<>(tagService.getListTag(), HttpStatus.OK);
    }
}
