package com.arpit.mythicgates.service.impl;

import com.arpit.mythicgates.helper.UserHelper;
import com.arpit.mythicgates.mapper.UserMapper;
import com.arpit.mythicgates.model.dto.user.UserProfileResponse;
import com.arpit.mythicgates.model.entity.User;
import com.arpit.mythicgates.repository.UserRepository;
import com.arpit.mythicgates.response.ApiResponse;
import com.arpit.mythicgates.response.ApiResponseUtil;
import com.arpit.mythicgates.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserHelper userHelper;

    @Override
    public void incrementBossDefeated(User user) {
        user.setBossesDefeated(user.getBossesDefeated() + 1);

        userRepository.save(user);
    }

    @Override
    public ResponseEntity<ApiResponse<UserProfileResponse>> getMe() {
        User user = userHelper.getCurrentUser();

        return ApiResponseUtil.success("User profile details fetched successfully", UserMapper.toUserProfileResponseDto(user));
    }
}
