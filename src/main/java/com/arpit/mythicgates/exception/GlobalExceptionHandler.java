package com.arpit.mythicgates.exception;

import com.arpit.mythicgates.exception.custom.*;
import com.arpit.mythicgates.response.ApiResponse;
import com.arpit.mythicgates.response.ApiResponseUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(UserAlreadyExistsException.class)
    public ResponseEntity<ApiResponse<String>> handleUserExists(UserAlreadyExistsException ex) {
        return ApiResponseUtil.conflict(null, ex.getMessage());
    }

    @ExceptionHandler(CharacterAlreadyExistsException.class)
    public ResponseEntity<ApiResponse<String>> handleCharacterExists(CharacterAlreadyExistsException ex) {
        return ApiResponseUtil.conflict(null, ex.getMessage());
    }

    @ExceptionHandler(BossAlreadyExistsException.class)
    public ResponseEntity<ApiResponse<String>> handleBossExists(BossAlreadyExistsException ex) {
        return ApiResponseUtil.conflict(null, ex.getMessage());
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiResponse<String>> handleNotFound(ResourceNotFoundException ex) {
        return ApiResponseUtil.notFound(null, ex.getMessage());
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ApiResponse<String>> handleBadRequest(BadRequestException ex) {
        return ApiResponseUtil.badRequest(null, ex.getMessage());
    }

    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<ApiResponse<String>> handleUnauthorizedRequest(UnauthorizedException ex) {
        return ApiResponseUtil.unauthorized(null, ex.getMessage());
    }

    @ExceptionHandler(ImageUploadFailedException.class)
    public ResponseEntity<ApiResponse<String>> handleImageUploadFail(ImageUploadFailedException ex) {
        return ApiResponseUtil.badRequest(null, ex.getMessage());
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<ApiResponse<String>> handleUsernameNotFound(UsernameNotFoundException ex) {
        return ApiResponseUtil.notFound(null, ex.getMessage());
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ApiResponse<String>> handleBadCredentialsException(BadCredentialsException ex) {
        return ApiResponseUtil.unauthorized("Username or password is incorrect", ex.getMessage());
    }

    @ExceptionHandler(AuthorizationDeniedException.class)
    public ResponseEntity<ApiResponse<String>> handleAuthorizationDeniedException(AuthorizationDeniedException ex) {
        return ApiResponseUtil.forbidden("Access denied", ex.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Map<String, String>>> handleValidation(
            MethodArgumentNotValidException ex) {

        Map<String, String> errors = new HashMap<>();

        ex.getBindingResult().getFieldErrors()
                .forEach(error ->
                        errors.put(error.getField(), error.getDefaultMessage()));

        return ApiResponseUtil.badRequest("Validation failed", errors);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<String>> handleGeneric(Exception ex) {
        ex.printStackTrace();
        return ApiResponseUtil.serverError(null, "Something Went Wrong");
    }
}
