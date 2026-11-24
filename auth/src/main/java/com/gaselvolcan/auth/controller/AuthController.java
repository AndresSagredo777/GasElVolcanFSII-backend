package com.gaselvolcan.auth.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.gaselvolcan.auth.dto.*;
import com.gaselvolcan.auth.service.AuthService;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

@RestController
@RequestMapping ("/auth") 
public class AuthController {
    @Autowired AuthService auth;

    @PostMapping ("/login")
    public LoginResponse login(@Valid @RequestBody LoginRequest request ){
        return auth.login(request);
    }

}
