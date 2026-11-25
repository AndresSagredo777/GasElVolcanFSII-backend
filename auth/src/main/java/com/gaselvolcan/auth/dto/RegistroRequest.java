package com.gaselvolcan.auth.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record RegistroRequest(
        @NotBlank @Size(max = 150) String nombre,
        @NotBlank @Email @Size(max = 255) String correo,
        @Size(max = 30) String telefono,
        @Size(max = 255) String direccion,
        @NotBlank @Size(min = 8, max = 72) String password) {
}
