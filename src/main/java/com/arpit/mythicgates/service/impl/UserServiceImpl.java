package com.arpit.mythicgates.service.impl;

import com.arpit.mythicgates.model.entity.User;
import com.arpit.mythicgates.repository.UserRepository;
import com.arpit.mythicgates.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    @Override
    public void incrementBossDefeated(User user) {
        user.setBossesDefeated(user.getBossesDefeated() + 1);

        userRepository.save(user);
    }
}
