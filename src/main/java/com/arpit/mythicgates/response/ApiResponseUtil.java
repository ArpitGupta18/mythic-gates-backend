package com.arpit.mythicgates.response;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class ApiResponseUtil {
    public static <T> ResponseEntity<ApiResponse<T>> success(String message, T data) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new ApiResponse<>(200, message, data, null));
    }

    public static <T> ResponseEntity<ApiResponse<T>> created(String message, T data) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new ApiResponse<>(201, message, data, null));
    }

    public static <T> ResponseEntity<ApiResponse<T>> badRequest(String message, T errors) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new ApiResponse<>(400, message, null, errors));
    }

    public static <T> ResponseEntity<ApiResponse<T>> unauthorized(String message, T errors) {
        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(new ApiResponse<>(401, message, null, errors));
    }

    public static <T> ResponseEntity<ApiResponse<T>> forbidden(String message, T errors) {
        return ResponseEntity
                .status(HttpStatus.FORBIDDEN)
                .body(new ApiResponse<>(403, message, null, errors));
    }

    public static <T> ResponseEntity<ApiResponse<T>> notFound(String message, T errors) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(new ApiResponse<>(404, message, null, errors));
    }

    public static <T> ResponseEntity<ApiResponse<T>> serverError(String message, T errors) {
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ApiResponse<>(500, message, null, errors));
    }

    public static <T> ResponseEntity<ApiResponse<T>> conflict(String message, T errors) {
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(new ApiResponse<>(409, message, null, errors));
    }
}
