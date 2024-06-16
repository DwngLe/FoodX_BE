package com.example.foodx_be.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

@Getter
public enum ErrorCode {
    UNCATEGORIZED_EXCEPTION(9999, "Uncategorized error", HttpStatus.INTERNAL_SERVER_ERROR),

    // for creation sth
    INVALID_FIELD(1001, "Uncategorized error", HttpStatus.BAD_REQUEST),
    INVALID_USERNAME(
            1002,
            "Username must be at least {min} characters, cannot contain special characters and cannot contain capital words",
            HttpStatus.BAD_REQUEST),
    INVALID_PASSWORD(1003, "Password must be at least {min} characters", HttpStatus.BAD_REQUEST),
    INVALID_DOB(1004, "Your age must be at least {min}", HttpStatus.BAD_REQUEST),
    PASSWORD_MISS_MATCH(1005, "Password and repeat password miss match", HttpStatus.BAD_REQUEST),
    REPEAT_PASSWORD_CANNOT_BE_EMPTY(1006, "Repeat password cannot be empty", HttpStatus.BAD_REQUEST),

    // for exist or not exist
    USER_EXISTED(2000, "User existed", HttpStatus.BAD_REQUEST),
    USER_NOT_EXISTED(2001, "User not existed", HttpStatus.NOT_FOUND),
    TAG_NOT_EXISTED(2002, "Restaurant's tag not existed", HttpStatus.NOT_FOUND),
    RESTAURANT_NOT_EXISTED(2003, "Restaurant not existed", HttpStatus.NOT_FOUND),
    BUSINESS_NOT_EXISTED(2004, "Business not existed", HttpStatus.NOT_FOUND),
    REVIEW_NOT_EXISTED(2005, "Cannot find any review", HttpStatus.NOT_FOUND),

    // for authen and author
    UNAUTHENTICATED(3000, "Cannot Authenticated", HttpStatus.UNAUTHORIZED),
    UNAUTHORIZED(3001, "You do not have permission", HttpStatus.FORBIDDEN),

    //for invalid param
    INVALID_PAGE_SIZE(4000, "Page size must be at least {min} objects", HttpStatus.BAD_REQUEST),
    INVALID_PAGE_NUMBER(4001, "Page cannot be negotive", HttpStatus.BAD_REQUEST);


    ErrorCode(int code, String message, HttpStatusCode statusCode) {
        this.code = code;
        this.message = message;
        this.statusCode = statusCode;
    }

    private final int code;
    private final String message;
    private final HttpStatusCode statusCode;
}
