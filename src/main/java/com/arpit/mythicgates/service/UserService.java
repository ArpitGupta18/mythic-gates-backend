package com.arpit.mythicgates.service;

import com.arpit.mythicgates.model.dto.user.UserProfileResponse;
import com.arpit.mythicgates.model.entity.User;
import com.arpit.mythicgates.response.ApiResponse;
import org.springframework.http.ResponseEntity;

public interface UserService {
    void incrementBossDefeated(User user);
    ResponseEntity<ApiResponse<UserProfileResponse>> getMe();
}
