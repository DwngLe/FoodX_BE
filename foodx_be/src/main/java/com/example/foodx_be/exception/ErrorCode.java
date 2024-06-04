package com.example.foodx_be.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

@Getter
public enum ErrorCode {
    UNCATEGORIZED_EXCEPTION(9999, "Uncategorized error", HttpStatus.INTERNAL_SERVER_ERROR),
    INVALID_KEY(1001, "Uncategorized error", HttpStatus.BAD_REQUEST),
    USER_EXISTED(1002, "User existed", HttpStatus.BAD_REQUEST),
    INVALID_USERNAME(1003, "Username must be at least {min} characters, cannot contain special characters and cannot contain capital words", HttpStatus.BAD_REQUEST),
    INVALID_PASSWORD(1004, "Password must be at least {min} characters", HttpStatus.BAD_REQUEST),
    USER_NOT_EXISTED(1005, "User not existed", HttpStatus.NOT_FOUND),
    TAG_NOT_EXISTED(1006, "Restaurant's tag not existed", HttpStatus.NOT_FOUND),

    RESTAURANT_NOT_EXISTED(1006, "Restaurant not existed", HttpStatus.NOT_FOUND),
    BUSINESS_NOT_EXISTED(1007, "Business not existed", HttpStatus.NOT_FOUND),

    UNAUTHENTICATED(2000, "Cannot Authenticated", HttpStatus.UNAUTHORIZED),
    UNAUTHORIZED(2001, "You do not have permission", HttpStatus.FORBIDDEN),
    INVALID_DOB(3008, "Your age must be at least {min}", HttpStatus.BAD_REQUEST),
    PASSWORD_MISS_MATCH(3009, "Password and repeat password miss match", HttpStatus.BAD_REQUEST),
    REPEAT_PASSWORD_CANNOT_BE_EMPTY(3010, "Repeat password cannot be empty", HttpStatus.BAD_REQUEST)
    ;

    ErrorCode(int code, String message, HttpStatusCode statusCode) {
        this.code = code;
        this.message = message;
        this.statusCode = statusCode;
    }

    private final int code;
    private final String message;
    private final HttpStatusCode statusCode;
}
