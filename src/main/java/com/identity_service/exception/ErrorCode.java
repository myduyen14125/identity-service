package com.identity_service.exception;

public enum ErrorCode {
    SUCCESS(200, "Success"),
    USER_NOT_FOUND(2, "User Not Found"),
    USER_ALREADY_EXISTS(3, "User Already Exists"),
    FAILURE(4, "Failure"),
    UNCATEGORIZED(5, "Uncategorized"),
    INVALID_PARAM(6, "Invalid Param"),
    INVALID_USERNAME(7, "Username must be between 3 to 20 characters"),
    INVALID_PASSWORD(8, "Password must be at least 6 characters"),
    INVALID_EMAIL(9, "Email must be available"),
    UNAUTHORIZED(10, "Unauthorized"),
    ;
    private int code;
    private String message;

    ErrorCode(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
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
