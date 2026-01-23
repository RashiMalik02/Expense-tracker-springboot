package com.expensetracker.expense_tracker.controller;

import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.expensetracker.expense_tracker.dto.AuthResponse;
import com.expensetracker.expense_tracker.dto.LoginRequest;
import com.expensetracker.expense_tracker.dto.RegisterRequest;
import com.expensetracker.expense_tracker.model.User;
import com.expensetracker.expense_tracker.security.JwtUtil;
import com.expensetracker.expense_tracker.service.UserService;

import io.swagger.v3.oas.annotations.parameters.RequestBody;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final UserService service;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public User register(@RequestBody @Valid RegisterRequest req){
        return service.register(req);
    }

    @PostMapping("/login")
    public AuthResponse login(@RequestBody @Valid LoginRequest req){
        authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(req.getEmail(), req.getPassword())
        );

        String token = jwtUtil.generateToken(req.getEmail());
        return new AuthResponse(token);
    }

}
