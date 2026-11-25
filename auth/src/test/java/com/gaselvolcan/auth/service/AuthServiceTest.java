package com.gaselvolcan.auth.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.gaselvolcan.auth.dto.RegistroRequest;
import com.gaselvolcan.auth.model.Usuario;
import com.gaselvolcan.auth.repository.AuthRepository;
import com.gaselvolcan.auth.security.JwtService;

class AuthServiceTest {
    private AuthRepository usuarios;
    private PasswordEncoder encoder;
    private JwtService jwt;
    private AuthService service;

    @BeforeEach
    void preparar() {
        usuarios = mock(AuthRepository.class);
        encoder = mock(PasswordEncoder.class);
        jwt = mock(JwtService.class);
        service = new AuthService(usuarios, encoder, jwt);
    }

    @Test
    void registroNormalizaCorreoCodificaClaveYAsignaRolUser() {
        when(usuarios.existsByCorreoIgnoreCase("ana@example.com")).thenReturn(false);
        when(encoder.encode("password-segura")).thenReturn("hash-bcrypt");
        when(usuarios.save(any(Usuario.class))).thenAnswer(invocacion -> invocacion.getArgument(0));
        when(jwt.generar(any(Usuario.class))).thenReturn("token-falso");

        var respuesta = service.registrar(new RegistroRequest(
                " Ana Pérez ", "ANA@EXAMPLE.COM ", " 912345678 ", " Calle 1 ", "password-segura"));

        assertEquals("ana@example.com", respuesta.correo());
        assertEquals("Ana Pérez", respuesta.nombre());
        assertEquals("USER", respuesta.rol());
        assertEquals("token-falso", respuesta.token());
        verify(encoder).encode("password-segura");
    }

    @Test
    void registroRechazaCorreoYaExistente() {
        when(usuarios.existsByCorreoIgnoreCase("ana@example.com")).thenReturn(true);

        assertThrows(CorreoYaRegistradoException.class, () -> service.registrar(
                new RegistroRequest("Ana", "ana@example.com", null, null, "password-segura")));
    }

    @Test
    void loginNormalizaCorreoYValidaConPasswordEncoder() {
        Usuario usuario = new Usuario();
        usuario.setCorreo("ana@example.com");
        usuario.setNombre("Ana");
        usuario.setPassword("hash-bcrypt");
        usuario.setRol("USER");
        when(usuarios.findByCorreo("ana@example.com")).thenReturn(Optional.of(usuario));
        when(encoder.matches("password-segura", "hash-bcrypt")).thenReturn(true);
        when(jwt.generar(usuario)).thenReturn("token-falso");

        var respuesta = service.login(new com.gaselvolcan.auth.dto.LoginRequest(" ANA@EXAMPLE.COM ", "password-segura"));

        assertEquals("ana@example.com", respuesta.correo());
        assertEquals("Ana", respuesta.nombre());
        assertEquals("token-falso", respuesta.token());
    }
}
