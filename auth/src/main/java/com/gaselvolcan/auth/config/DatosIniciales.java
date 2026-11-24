package com.gaselvolcan.auth.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.gaselvolcan.auth.model.Usuario;
import com.gaselvolcan.auth.repository.AuthRepository;

@Configuration
public class DatosIniciales {
    @Bean CommandLineRunner usuarioInicial (AuthRepository repo, PasswordEncoder encoder) { 
        return args -> {
            if (repo.findByCorreo("admin@gaselvolcan.cl").isEmpty()) {
                Usuario u = new Usuario();
                u.setCorreo("admin@gaselvolcan.cl");
                u.setPassword(encoder.encode("Cambiar123!"));
                u.setRol("ADMIN");
                repo.save(u);
            }
        };
    }
      
}
