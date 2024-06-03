package com.example.foodx_be.controller;

import com.example.foodx_be.dto.request.AuthenticationRequest;
import com.example.foodx_be.dto.request.UserCreationRequest;
import com.example.foodx_be.dto.response.AuthenticationResponse;
import com.example.foodx_be.dto.response.UserResponse;
import com.example.foodx_be.service.AuthenticationService;
import com.example.foodx_be.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Controller
@RestController
@RequestMapping("/auth")
@AllArgsConstructor
public class AuthenticationController {
    private UserService userService;
    private AuthenticationService authenticationService;


    @Operation(
            description = "Người dùng gửi lên các thông tin cần thiết để đăng ký tài khoản, hệ thống sẽ trả về các thông tin cơ bản",
            summary = "Đăng ký tài khoản",
            responses = {
                    @ApiResponse(
                            description = "Thành công",
                            responseCode = "200"
                    ),
                    @ApiResponse(
                            description = "Thông tin không hợp lệ",
                            responseCode = "405"
                    )
            }
    )
    @PostMapping("/register")
    private ResponseEntity<UserResponse> register(@Valid @RequestBody UserCreationRequest userCreationRequest) {
        return new ResponseEntity<>(userService.saveUser(userCreationRequest), HttpStatus.CREATED);
    }

    @Operation(
            description = "Người dùng gửi lên các thông tin cần thiết để đăng nhập tài khoản, hệ thống sẽ trả về token",
            summary = "Đăng nhập tài khoản",
            responses = {
                    @ApiResponse(
                            description = "Thành công",
                            responseCode = "200"
                    ),
                    @ApiResponse(
                            description = "Thông tin không hợp lệ",
                            responseCode = "405"
                    )
            }
    )
    @PostMapping("/login")
    private ResponseEntity<AuthenticationResponse> authenticate(@RequestBody AuthenticationRequest request) {
        return new ResponseEntity<>(authenticationService.authenticate(request), HttpStatus.OK);
    }
}
