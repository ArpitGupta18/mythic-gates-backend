package com.arpit.mythicgates.service.impl;

import com.arpit.mythicgates.exception.custom.BadRequestException;
import com.arpit.mythicgates.exception.custom.ResourceNotFoundException;
import com.arpit.mythicgates.model.entity.User;
import com.arpit.mythicgates.repository.UserRepository;
import com.arpit.mythicgates.service.EconomyService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class EconomyServiceImpl implements EconomyService {
    private final UserRepository userRepository;

    @Override
    public void addGold(String username, int amount) {
        if (amount <= 0) {
            throw new BadRequestException("Amount must be greater than 0");
        }

        User user = userRepository.findByUsernameIgnoreCase(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        user.setGold(user.getGold() + amount);

        userRepository.save(user);
    }

    @Override
    public void deductGold(String username, int amount) {
        if (amount <= 0){
            throw new BadRequestException("Amount must be greater than 0");
        }

        User user = userRepository.findByUsernameIgnoreCase(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        int gold = user.getGold() - amount;

        if (gold < 0) {
            throw new BadRequestException("Not enough gold in your account to deduct");
        }
        user.setGold(gold);

        userRepository.save(user);
    }

    @Override
    public int getUserBalance(String username) {
        User user = userRepository.findByUsernameIgnoreCase(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        return user.getGold() ;
    }
}
