package com.localhelp.backend.controller;

import com.localhelp.backend.dto.LoginRequest;
import com.localhelp.backend.dto.LoginResponse;
import com.localhelp.backend.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@CrossOrigin
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/login")
    public LoginResponse login(@RequestBody LoginRequest request) {
        return authService.login(request);
    }

    @PostMapping("/logout")
    public LoginResponse logout() {
        return authService.logout();
    }
}