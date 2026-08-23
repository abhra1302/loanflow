package com.loanflow.loan_service.auth;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.loanflow.loan_service.auth.dto.JwtResponse;
import com.loanflow.loan_service.auth.dto.LoginRequest;

@RestController
@RequestMapping("/auth")
public class AuthController {
 private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public JwtResponse login(@RequestBody LoginRequest request) {
        return authService.login(request);
    }
}
