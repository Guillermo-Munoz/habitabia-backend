package com.habitia.shared.infrastructure.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    public SecurityConfig(JwtAuthenticationFilter jwtAuthenticationFilter) {
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
        System.out.println("⭐⭐⭐ CONFIGURACIÓN DE SEGURIDAD CARGADA ⭐⭐⭐");
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        System.out.println("🔐 PasswordEncoder BCrypt creado");
        return new BCryptPasswordEncoder();  // ← ¡Esto es lo que faltaba!
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        System.out.println("🔧 Configurando SecurityFilterChain...");

        return http
                .csrf(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable)
                .sessionManagement(s -> s.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> {
                    // 1. Auth pública
                    auth.requestMatchers("/api/v1/auth/**").permitAll();

                    // 2. Swagger y OpenAPI (Asegúrate de incluir los recursos estáticos)
                    auth.requestMatchers(
                        "/v3/api-docs/**",
                        "/v3/api-docs.yaml",
                        "/swagger-ui/**",
                        "/swagger-ui.html",
                        "/swagger-resources/**",
                        "/webjars/**"
                    ).permitAll();

                    // 3. Scalar (La ruta que creamos para tu interfaz moderna)
                    auth.requestMatchers("/docs", "/scalar.html").permitAll();

                    // 4. Todo lo demás protegido
                    auth.anyRequest().authenticated();
                    
                    System.out.println("📋 Reglas de autorización configuradas con Swagger y Scalar");
                })
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }
}