package com.example.foodx_be.controller;

import com.example.foodx_be.dto.RegisterCommand;
import com.example.foodx_be.dto.UserDTO;
import com.example.foodx_be.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
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
public class AuthController {
    @Autowired
    private UserService userService;

    @PostMapping("/register")
    private ResponseEntity<UserDTO> register(@Valid @RequestBody RegisterCommand registerCommand) {
        return new ResponseEntity<>(userService.saveUser(registerCommand), HttpStatus.CREATED);
    }
}
