package com.arpit.mythicgates.service;

import java.util.UUID;

public interface EconomyService {
    void addGold(String username, int amount);
    void deductGold(String username, int amount);
    int getUserBalance(String username);
}
