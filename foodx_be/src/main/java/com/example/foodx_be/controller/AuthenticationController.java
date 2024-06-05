package com.example.foodx_be.controller;

import com.example.foodx_be.dto.request.AuthenticationRequest;
import com.example.foodx_be.dto.request.IntrospectRequest;
import com.example.foodx_be.dto.request.RefeshRequest;
import com.example.foodx_be.dto.request.UserCreationRequest;
import com.example.foodx_be.dto.response.AuthenticationResponse;
import com.example.foodx_be.dto.response.IntrospectResponse;
import com.example.foodx_be.dto.response.UserResponse;
import com.example.foodx_be.exception.APIResponse;
import com.example.foodx_be.service.AuthenticationService;
import com.example.foodx_be.service.UserService;
import com.nimbusds.jose.JOSEException;
import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;

@Controller
@RestController
@Tag(name = "Authentication")
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
    private APIResponse<UserResponse> register(@Valid @RequestBody UserCreationRequest userCreationRequest) {
        return APIResponse.<UserResponse>builder()
                .result(userService.saveUser(userCreationRequest))
                .build();
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
                    ),
                    @ApiResponse(
                            description = "Xác thực không thành công",
                            responseCode = "401"
                    )
            }
    )
    @PostMapping("/login")
    private APIResponse<AuthenticationResponse> authenticate(@RequestBody AuthenticationRequest request) {
        return APIResponse.<AuthenticationResponse>builder()
                .result(authenticationService.authenticate(request))
                .build();
    }

    @Operation(
            summary = "Đăng xuất tài khoản",
            responses = {
                    @ApiResponse(
                            description = "Thành công",
                            responseCode = "200"
                    )
            }
    )
    @PostMapping("/logout")
    private APIResponse<Void> logout() throws ParseException, JOSEException {
        authenticationService.logout();
        return APIResponse.<Void>builder().build();
    }

    @Hidden
    @PostMapping("/introspect")
    private APIResponse<IntrospectResponse> authenticate(@RequestBody IntrospectRequest request)
            throws ParseException, JOSEException {
        var result = authenticationService.introspect(request);
        return APIResponse.<IntrospectResponse>builder().result(result).build();
    }

    @Operation(
            description = "Người dùng gửi lên token đã hết hạn, hệ thống sẽ trả về token mới (token cũ phải còn trong thời hạn refresh)",
            summary = "Refresh token",
            responses = {
                    @ApiResponse(
                            description = "Thành công",
                            responseCode = "200"
                    ),
                    @ApiResponse(
                            description = "Xác thực không thành công",
                            responseCode = "401"
                    )
            }
    )
    @SecurityRequirement(name = "bearAuth")
    @PostMapping("/refresh")
    private APIResponse<AuthenticationResponse> refreshToken(@RequestBody RefeshRequest request) throws ParseException, JOSEException {
        return APIResponse.<AuthenticationResponse>builder()
                .result(authenticationService.refeshToken(request))
                .build();
    }
}
