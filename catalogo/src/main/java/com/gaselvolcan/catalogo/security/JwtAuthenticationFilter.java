package com.gaselvolcan.catalogo.security;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;

    public JwtAuthenticationFilter(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {

        String authorization = request.getHeader("Authorization");

        if (authorization != null && authorization.startsWith("Bearer ")) {
            String token = authorization.substring(7);
            try {
                Claims claims = jwtService.validar(token);
                String correo = claims.getSubject();
                String rol = claims.get("rol", String.class);

                if (correo != null && rol != null
                        && SecurityContextHolder.getContext().getAuthentication() == null) {
                    String nombreRol = rol.toUpperCase(Locale.ROOT);
                    if (nombreRol.startsWith("ROLE_")) {
                        nombreRol = nombreRol.substring(5);
                    }

                    var autenticacion = new UsernamePasswordAuthenticationToken(
                            correo,
                            null,
                            List.of(new SimpleGrantedAuthority("ROLE_" + nombreRol)));
                    SecurityContextHolder.getContext().setAuthentication(autenticacion);
                }
            } catch (JwtException | IllegalArgumentException error) {
                SecurityContextHolder.clearContext();
            }
        }

        filterChain.doFilter(request, response);
    }
}
