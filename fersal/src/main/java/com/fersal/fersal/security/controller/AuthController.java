package com.fersal.fersal.security.controller;


import com.fersal.fersal.security.dto.BiometricRequest;
import com.fersal.fersal.security.dto.LoginRequest;
import com.fersal.fersal.security.dto.LoginResponse;
import com.fersal.fersal.security.service.AuthService;


import lombok.RequiredArgsConstructor;


import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {


    private final AuthService authService;


    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request) {

        return ResponseEntity.ok(authService.login(request));

    }

    @PostMapping("/biometric")
    public ResponseEntity<LoginResponse> biometric(@RequestBody BiometricRequest request) {

        return ResponseEntity.ok(authService.biometricLogin(request.getEmail()));

    }


}