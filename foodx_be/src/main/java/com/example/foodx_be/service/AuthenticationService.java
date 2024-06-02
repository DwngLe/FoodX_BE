package com.example.foodx_be.service;

import com.example.foodx_be.dto.request.AuthenticationRequest;
import com.example.foodx_be.dto.response.AuthenticationResponse;

public interface AuthenticationService {
    AuthenticationResponse authenticate(AuthenticationRequest authenticationRequest);
}
