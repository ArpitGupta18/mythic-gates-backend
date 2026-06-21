package com.arpit.mythicgates.helper;

import com.arpit.mythicgates.model.entity.User;
import com.arpit.mythicgates.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserHelper {
    private final UserRepository userRepository;

    public User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        assert authentication != null;
        String username = authentication.getName();

        return userRepository.findByUsernameIgnoreCase(username).orElseThrow(
                () -> new UsernameNotFoundException("User not found")
        );
    }
}
