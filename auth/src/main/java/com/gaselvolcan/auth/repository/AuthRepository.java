package com.gaselvolcan.auth.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import com.gaselvolcan.auth.model.Usuario;

public interface AuthRepository extends JpaRepository <Usuario, Long>{
    Optional<Usuario> findByCorreo(String correo);
}
