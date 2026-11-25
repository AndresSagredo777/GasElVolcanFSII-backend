package com.gaselvolcan.auth.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.beans.factory.annotation.Value;

import com.gaselvolcan.auth.model.Usuario;
import com.gaselvolcan.auth.repository.AuthRepository;

@Configuration
public class DatosIniciales {
    @Bean CommandLineRunner usuarioInicial(
            AuthRepository repo,
            PasswordEncoder encoder,
            @Value("${app.admin.initial-email:admin@gaselvolcan.cl}") String correo,
            @Value("${app.admin.initial-password:admin123}") String password,
            @Value("${app.admin.initial-name:Administrador}") String nombre) {
        return args -> {
            if (correo == null || correo.isBlank() || password == null || password.isBlank()) {
                return;
            }
            String correoNormalizado = correo.trim().toLowerCase(java.util.Locale.ROOT);
            if (repo.findByCorreo(correoNormalizado).isEmpty()) {
                Usuario u = new Usuario();
                u.setCorreo(correoNormalizado);
                u.setNombre(nombre.trim());
                u.setPassword(encoder.encode(password));
                u.setRol("ADMIN");
                repo.save(u);
            }
        };
    }
}
