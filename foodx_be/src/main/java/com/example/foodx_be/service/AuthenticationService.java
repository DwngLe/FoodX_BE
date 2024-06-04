package com.example.foodx_be.service;

import com.example.foodx_be.dto.request.AuthenticationRequest;
import com.example.foodx_be.dto.request.IntrospectRequest;
import com.example.foodx_be.dto.request.LogoutRequest;
import com.example.foodx_be.dto.request.RefeshRequest;
import com.example.foodx_be.dto.response.AuthenticationResponse;
import com.example.foodx_be.dto.response.IntrospectResponse;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jwt.SignedJWT;

import java.text.ParseException;

public interface AuthenticationService {
    AuthenticationResponse authenticate(AuthenticationRequest authenticationRequest);
    void logout(LogoutRequest request) throws ParseException, JOSEException;

    SignedJWT verifyToken(String token, boolean isRefresh) throws JOSEException, ParseException;
    IntrospectResponse introspect(IntrospectRequest introspectRequest) throws ParseException, JOSEException;

    AuthenticationResponse refeshToken(RefeshRequest request) throws ParseException, JOSEException;

}
