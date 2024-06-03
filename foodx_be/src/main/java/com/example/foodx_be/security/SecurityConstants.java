package com.example.foodx_be.security;

public class SecurityConstants {
    public static final String SECRET_KEY = "lpRIVeLqP1fVr28Z06053yE66Jc2BnlyQ7QEeuZv6KL+CiO7Ucz3tlafKOPPm6f7m";
    public static final int TOKEN_EXPIRATION = 7200000; // 7200000 milliseconds = 7200 seconds = 2 hours.
    public static final String BEARER = "Bearer "; // Authorization : "Bearer " + Token
    public static final String AUTHORIZATION = "Authorization"; // "Authorization" : Bearer Token
    public static final String REGISTER_PATH = "/auth/register";
}
