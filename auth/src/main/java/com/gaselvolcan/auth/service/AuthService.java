package com.gaselvolcan.auth.service;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gaselvolcan.auth.dto.LoginRequest;
import com.gaselvolcan.auth.dto.LoginResponse;
import com.gaselvolcan.auth.dto.RegistroRequest;
import com.gaselvolcan.auth.model.Usuario;
import com.gaselvolcan.auth.repository.AuthRepository;
import com.gaselvolcan.auth.security.JwtService;

@Service 
public class AuthService {
    private final AuthRepository usuarios;
    private final PasswordEncoder encoder;
    private final JwtService jwt;

    public AuthService(AuthRepository usuarios, PasswordEncoder encoder, JwtService jwt) {
        this.usuarios = usuarios;
        this.encoder = encoder;
        this.jwt = jwt;
    }

    @Transactional(readOnly = true)
    public LoginResponse login(LoginRequest request) {
        String correo = request.correo().trim().toLowerCase(java.util.Locale.ROOT);
        Usuario usuario = usuarios.findByCorreo(correo)
        .orElseThrow(() -> new BadCredentialsException("Credenciales inválidas"));

        if (!encoder.matches(request.password(), usuario.getPassword())) {
        throw new BadCredentialsException("Credenciales inválidas");
        }

        return respuesta(usuario);
    }

    @Transactional
    public LoginResponse registrar(RegistroRequest request) {
        String correo = request.correo().trim().toLowerCase(java.util.Locale.ROOT);
        if (usuarios.existsByCorreoIgnoreCase(correo)) {
            throw new CorreoYaRegistradoException();
        }

        Usuario usuario = new Usuario();
        usuario.setCorreo(correo);
        usuario.setNombre(request.nombre().trim());
        usuario.setTelefono(limpiar(request.telefono()));
        usuario.setDireccion(limpiar(request.direccion()));
        usuario.setPassword(encoder.encode(request.password()));
        usuario.setRol("USER");
        usuario = usuarios.save(usuario);
        return respuesta(usuario);
    }

    private LoginResponse respuesta(Usuario usuario) {
        return new LoginResponse(jwt.generar(usuario), usuario.getCorreo(), usuario.getNombre(), usuario.getRol());
    }

    private String limpiar(String valor) {
        return valor == null || valor.isBlank() ? null : valor.trim();
    }
}
