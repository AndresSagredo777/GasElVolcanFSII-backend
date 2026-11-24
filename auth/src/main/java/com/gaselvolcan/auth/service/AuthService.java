package com.gaselvolcan.auth.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.gaselvolcan.auth.dto.LoginRequest;
import com.gaselvolcan.auth.dto.LoginResponse;
import com.gaselvolcan.auth.model.Usuario;
import com.gaselvolcan.auth.repository.AuthRepository;
import com.gaselvolcan.auth.security.JwtService;

@Service 
public class AuthService {
    private final AuthRepository usuarios;
    @Autowired PasswordEncoder encoder;
    @Autowired JwtService jwt;

    public AuthService(AuthRepository usuarios) {
        this.usuarios = usuarios;
    }
    
    public LoginResponse login(LoginRequest request) {
        Usuario usuario = usuarios.findByCorreo(request.correo())
        .orElseThrow(() -> new BadCredentialsException("Credenciales inválidas"));

        if (!encoder.matches(request.password(), usuario.getPassword())) {
        throw new BadCredentialsException("Credenciales inválidas");
        }

        return new LoginResponse(jwt.generar(usuario),
        usuario.getCorreo(), usuario.getRol());
    }
}
