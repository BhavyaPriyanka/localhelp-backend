package com.localhelp.backend.service;

import com.localhelp.backend.dto.LoginRequest;
import com.localhelp.backend.dto.LoginResponse;
import com.localhelp.backend.model.User;
import com.localhelp.backend.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.localhelp.backend.security.JwtService;

import java.time.LocalDateTime;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthService(UserRepository userRepository,
                   PasswordEncoder passwordEncoder,
                   JwtService jwtService) {
    this.userRepository = userRepository;
    this.passwordEncoder = passwordEncoder;
    this.jwtService = jwtService;
    }

    public LoginResponse login(LoginRequest request) {

        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new RuntimeException("Invalid username or password"));

        if (!user.getEnabled()) {
            throw new RuntimeException("User account is disabled");
        }

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid username or password");
        }

        user.setLastLogin(LocalDateTime.now());
        userRepository.save(user);

        String token = jwtService.generateToken(user.getUsername());

        return new LoginResponse(
                true,
                "Login successful",
                user.getUsername(),
                user.getRole().name(),
                token
                    );
    }

    public LoginResponse logout() {

        return new LoginResponse(
                true,
                "Logout successful",
                null,
                null,
                null
        );
    }
}