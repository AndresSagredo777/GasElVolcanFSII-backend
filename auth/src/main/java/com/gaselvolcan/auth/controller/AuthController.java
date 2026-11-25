package com.gaselvolcan.auth.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import com.gaselvolcan.auth.dto.*;
import com.gaselvolcan.auth.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping ("/auth") 
public class AuthController {
    private final AuthService auth;

    public AuthController(AuthService auth) {
        this.auth = auth;
    }

    @PostMapping ("/login")
    public LoginResponse login(@Valid @RequestBody LoginRequest request ){
        return auth.login(request);
    }

    @PostMapping("/registro")
    public ResponseEntity<LoginResponse> registrar(@Valid @RequestBody RegistroRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(auth.registrar(request));
    }

}
