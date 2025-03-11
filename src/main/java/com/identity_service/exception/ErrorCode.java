package com.identity_service.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

@Getter
public enum ErrorCode {
    SUCCESS(200, "Success", HttpStatus.OK),
    USER_NOT_FOUND(2, "User Not Found", HttpStatus.NOT_FOUND),
    USER_ALREADY_EXISTS(3, "User Already Exists", HttpStatus.BAD_REQUEST),
    FAILURE(4, "Failure", HttpStatus.INTERNAL_SERVER_ERROR),
    UNCATEGORIZED(5, "Uncategorized", HttpStatus.INTERNAL_SERVER_ERROR),
    INVALID_PARAM(6, "Invalid Param", HttpStatus.BAD_REQUEST),
    INVALID_USERNAME(7, "Username must be between 3 to 20 characters", HttpStatus.BAD_REQUEST),
    INVALID_PASSWORD(8, "Password must be at least 6 characters", HttpStatus.BAD_REQUEST),
    INVALID_EMAIL(9, "Email must be available", HttpStatus.BAD_REQUEST),
    UNAUTHORIZED(10, "You do not have permission", HttpStatus.FORBIDDEN), // 403
    UNAUTHENTICATED(11, "Unauthenticated", HttpStatus.UNAUTHORIZED), // 401
    ;
    private int code;
    private String message;
    private HttpStatusCode httpStatusCode;

    ErrorCode(int code, String message) {
        this.code = code;
        this.message = message;
    }

    ErrorCode(int code, String message, HttpStatusCode httpStatusCode) {
        this.code = code;
        this.message = message;
        this.httpStatusCode = httpStatusCode;
    }

    public static String fromCode(int code) {
        for (ErrorCode errorCode : ErrorCode.values()) {
            if (errorCode.code == code) {
                return errorCode.message;
            }
        }
        throw new IllegalArgumentException("Invalid role value: " + code);
    }
}
