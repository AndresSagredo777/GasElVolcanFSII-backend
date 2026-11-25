package com.gaselvolcan.auth.config;

import java.util.List;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.*;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.*;

@Configuration 
public class SecurityConfig {
    @Bean PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    return http.csrf(csrf -> csrf.disable())
      .cors(Customizer.withDefaults())
      .authorizeHttpRequests(auth -> auth
        .requestMatchers("/auth/login", "/auth/registro", "/swagger-ui/**",
          "/swagger-ui.html", "/v3/api-docs/**").permitAll()
        .anyRequest().denyAll())
      .build();
    }

    @Bean CorsConfigurationSource corsConfigurationSource(
            @Value("${app.cors.allowed-origin:http://localhost:5173}") String allowedOrigin) {
        CorsConfiguration c = new CorsConfiguration();
        c.setAllowedOrigins(List.of(allowedOrigin));
        c.setAllowedMethods(List.of("POST", "OPTIONS"));
        c.setAllowedHeaders(List.of("Content-Type"));
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/auth/**", c);
        return source;
    } 
}
