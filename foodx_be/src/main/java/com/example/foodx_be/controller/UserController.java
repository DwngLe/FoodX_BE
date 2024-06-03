package com.example.foodx_be.controller;

import com.example.foodx_be.dto.request.AddBusinessProofCommand;
import com.example.foodx_be.dto.request.UpdateUserComand;
import com.example.foodx_be.dto.response.UserDTO;
import com.example.foodx_be.enity.User;
import com.example.foodx_be.repository.UserRepository;
import com.example.foodx_be.service.BusinessProofService;
import com.example.foodx_be.service.FiltersSpecificationImpl;
import com.example.foodx_be.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@AllArgsConstructor
@RestController
@RequestMapping("/users")
public class UserController {
    private UserService userService;
    private BusinessProofService businessProofService;

    private UserRepository userRepository;
    private FiltersSpecificationImpl<User> specification;
    @Operation(
            summary = "Lấy ra thông tin của 1 người dùng",
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
    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> findById(@PathVariable UUID id) {
        return new ResponseEntity<>(userService.getUserByID(id), HttpStatus.OK);
    }

    @Operation(
            summary = "Cập nhật thông tin cá nhân của người dùng",
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
    @PostMapping("/{id}")
    public ResponseEntity<UserDTO> updateUser(@RequestBody UpdateUserComand updateUserComand) {
        return new ResponseEntity<>(userService.updateUser(updateUserComand), HttpStatus.OK);
    }


    @Operation(
            summary = "Lấy ra danh sách các người dùng dựa trên tên người dùng",
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
    public ResponseEntity<Page<UserDTO>> findUsersByName(@RequestParam(name = "name") String name,
                                                         @RequestParam(name = "pageNo", defaultValue = "0") int pageNo,
                                                         @RequestParam(name = "limit", defaultValue = "5") int limit) {
        return new ResponseEntity<>(userService.getUsersByName(pageNo, limit, name), HttpStatus.OK);
    }

    @Operation(
            summary = "Cập nhật ảnh cá nhân của người dùng",
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
    @PostMapping("/avatar/{idUser}")
    public ResponseEntity<HttpStatus> updateUserAvatar(@RequestParam MultipartFile multipartFile,
                                                       @PathVariable UUID idUser) throws IOException {
        userService.updateUserAvatar(idUser, multipartFile);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/myInfo")
    public ResponseEntity<UserDTO> getMyInfo() {
        return new ResponseEntity<>(userService.getMyInfo(), HttpStatus.OK);
    }


    @Operation(
            summary = "Gừi yêu cầu xác thực nhà hàng",
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
                            description = "Yêu cầu nộp bằng chứng",
                            responseCode = "405"
                    )
            }

    )
    @PostMapping("/claimBusiness/{idRestaurant}")
    public ResponseEntity<HttpStatus> addBusinessProof(@PathVariable UUID idRestaurant,
                                                       @RequestPart("data") AddBusinessProofCommand addBusinessProofCommand,
                                                       @RequestPart(value = "multipartFile") MultipartFile multipartFile) throws IOException {
        addBusinessProofCommand.setIdRestaurant(idRestaurant);
        businessProofService.addBusinessProof(addBusinessProofCommand, multipartFile);
        return  new ResponseEntity<>(HttpStatus.OK);
    }


}
