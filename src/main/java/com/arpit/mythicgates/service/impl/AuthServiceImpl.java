package com.arpit.mythicgates.service.impl;

import com.arpit.mythicgates.exception.custom.BadRequestException;
import com.arpit.mythicgates.exception.custom.UserAlreadyExistsException;
import com.arpit.mythicgates.mapper.AuthMapper;
import com.arpit.mythicgates.model.dto.auth.*;
import com.arpit.mythicgates.model.entity.Character;
import com.arpit.mythicgates.model.entity.User;
import com.arpit.mythicgates.model.entity.UserCharacter;
import com.arpit.mythicgates.model.enums.Role;
import com.arpit.mythicgates.repository.CharacterRepository;
import com.arpit.mythicgates.repository.UserCharacterRepository;
import com.arpit.mythicgates.repository.UserRepository;
import com.arpit.mythicgates.response.ApiResponse;
import com.arpit.mythicgates.response.ApiResponseUtil;
import com.arpit.mythicgates.service.AuthService;
import com.arpit.mythicgates.utils.UuidGenerator;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final CustomUserDetailsService userDetailsService;
    private final JwtService jwtService;
    private final CharacterRepository characterRepository;
    private final UserCharacterRepository userCharacterRepository;

    @Override
    @Transactional
    public ResponseEntity<ApiResponse<RegisterResponse>> register(RegisterRequest request) {
        String username = request.username().trim();
        String email = request.email().trim().toLowerCase();
        if (userRepository.existsByUsernameIgnoreCase(username)) {
            throw new UserAlreadyExistsException("User already exists with this username");
        }

        if (userRepository.existsByEmail(email)) {
            throw new UserAlreadyExistsException("User already exists with this email");
        }

        Integer gold = 0;

        User user = AuthMapper.toEntity(request);

        user.setPublicId(UuidGenerator.generate());
        user.setPassword(passwordEncoder.encode(request.password()));
        user.setRole(Role.ROLE_USER);
        user.setGold(gold);
        user.setBossesDefeated(0);

        User savedUser = userRepository.save(user);

        assignStarterCharacters(savedUser);

        RegisterResponse response = AuthMapper.toRegisterResponseDto(savedUser);
        return ApiResponseUtil.created("User registered successfully", response);
    }

    @Override
    public ResponseEntity<ApiResponse<LoginResponse>> login(LoginRequest request) {
        String username = request.username().trim();
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        username,
                        request.password()
                )
        );

        UserDetails userDetails = userDetailsService
                .loadUserByUsername(request.username());

        String accessToken = jwtService.generateAccessToken(userDetails);
        String refreshToken = jwtService.generateRefreshToken(userDetails);

        LoginResponse response = new LoginResponse(accessToken, refreshToken);

        return ApiResponseUtil.success("Login success", response);
    }

    @Override
    public ResponseEntity<ApiResponse<LoginResponse>> refreshToken(RefreshTokenRequest request) {
        String refreshToken = request.refreshToken();

        String username = jwtService.extractUsername(refreshToken);

        UserDetails userDetails =
                userDetailsService.loadUserByUsername(username);

        if (!jwtService.isTokenValid(refreshToken, userDetails)) {
            throw new RuntimeException("Invalid refresh token");
        }

        String newAccessToken = jwtService.generateAccessToken(userDetails);

        LoginResponse response = new LoginResponse(newAccessToken, refreshToken);

        return ApiResponseUtil.success(
                "Token refreshed successfully",
                response
        );
    }

    private void assignStarterCharacters(User user) {
        List<Character> starterCharacters = characterRepository.findByIsStarterTrue();

        if (starterCharacters.size() != 3) {
            throw new BadRequestException("Exactly 3 starter characters must be configured");
        }

        List<UserCharacter> userCharacters = starterCharacters.stream()
                .map(character -> UserCharacter.builder()
                        .publicId(UuidGenerator.generate())
                        .user(user)
                        .character(character)
                        .obtainedAt(LocalDateTime.now())
                        .build()
                )
                .toList();

        userCharacterRepository.saveAll(userCharacters);
    }

}
