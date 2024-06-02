package com.example.foodx_be.service;

import com.example.foodx_be.dto.request.AuthenticationRequest;
import com.example.foodx_be.dto.response.AuthenticationResponse;
import com.example.foodx_be.exception.UnAuthenticatedException;
import com.example.foodx_be.exception.UserNotFoundException;
import com.example.foodx_be.repository.UserRepository;
import com.example.foodx_be.security.SecurityConstants;
import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jwt.JWTClaimsSet;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

@Service
@AllArgsConstructor
public class AuthenticationImpl implements AuthenticationService {
    private UserRepository userRepository;
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    public AuthenticationResponse authenticate(AuthenticationRequest authenticationRequest) {
        var user = userRepository.findByUsername(authenticationRequest.getUsername())
                .orElseThrow(() -> new UserNotFoundException());
        Boolean authenticated = bCryptPasswordEncoder.matches(authenticationRequest.getPassword(), user.getPassword());
        if (!authenticated) {
            throw new UnAuthenticatedException();
        }
        var token = generateToken(authenticationRequest.getUsername());
        return AuthenticationResponse.builder()
                .token(token)
                .authenticated(true)
                .build();
    }

    private String generateToken(String username) {
        JWSHeader jwsHeader = new JWSHeader(JWSAlgorithm.HS512);
        //data trong body dc goi la claim
        //claim tieu chuan:
        JWTClaimsSet jwtClaimsSet = new JWTClaimsSet.Builder()
                .subject(username)
                .issuer("DuongLe")//issue tu ai, thuong la domain service
                .issueTime(new Date())
                .expirationTime(new Date(Instant.now().plus(1, ChronoUnit.HOURS).toEpochMilli()))
                .build();

        Payload payload = new Payload(jwtClaimsSet.toJSONObject());

        JWSObject jwsObject = new JWSObject(jwsHeader, payload);

        try {
            //MAC: khoa giai ma va khoa ky la 1
            jwsObject.sign(new MACSigner(SecurityConstants.SECRET_KEY.getBytes()));
            return jwsObject.serialize();
        } catch (JOSEException e) {
            throw new RuntimeException(e);
        }
    }
}
