package com.estudiomusical.security;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class WebSecurityConfig {
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    private final UserDetailsService jwtUserDetailsService;
    private final JwtRequestFilter jwtRequestFilter;

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public static PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(jwtUserDetailsService).passwordEncoder(passwordEncoder());
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(AbstractHttpConfigurer::disable)
                .cors(cors -> {})
                .formLogin(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(req -> req
                        .requestMatchers("/login").permitAll()
                        .requestMatchers("/reportes/**").hasAuthority("ADMIN")
                        .requestMatchers("/pagos/**").hasAnyAuthority("ADMIN", "INGENIERO")
                        // CLIENTE puede leer salas, servicios, equipos y clientes (para el formulario de reservas)
                        .requestMatchers(HttpMethod.GET, "/clientes/**").hasAnyAuthority("ADMIN", "INGENIERO", "CLIENTE")
                        .requestMatchers(HttpMethod.GET, "/salas/**").hasAnyAuthority("ADMIN", "INGENIERO", "CLIENTE")
                        .requestMatchers(HttpMethod.GET, "/servicios/**").hasAnyAuthority("ADMIN", "INGENIERO", "CLIENTE")
                        .requestMatchers(HttpMethod.GET, "/equipos/**").hasAnyAuthority("ADMIN", "INGENIERO", "CLIENTE")
                        // Escritura solo ADMIN e INGENIERO
                        .requestMatchers("/clientes/**").hasAnyAuthority("ADMIN", "INGENIERO")
                        .requestMatchers("/salas/**").hasAnyAuthority("ADMIN", "INGENIERO")
                        .requestMatchers("/servicios/**").hasAnyAuthority("ADMIN", "INGENIERO")
                        .requestMatchers("/equipos/**").hasAnyAuthority("ADMIN", "INGENIERO")
                        // El CLIENTE solo puede ver SUS reservas y crear una reserva (a su nombre)
                        .requestMatchers(HttpMethod.GET, "/reservas/mias").hasAnyAuthority("ADMIN", "INGENIERO", "CLIENTE")
                        .requestMatchers(HttpMethod.POST, "/reservas").hasAnyAuthority("ADMIN", "INGENIERO", "CLIENTE")
                        // El resto de operaciones de reservas (ver todas, editar, cancelar, eliminar) es solo staff
                        .requestMatchers("/reservas/**").hasAnyAuthority("ADMIN", "INGENIERO")
                        .requestMatchers("/menus/**").hasAnyAuthority("ADMIN", "INGENIERO", "CLIENTE")
                        .anyRequest().authenticated()
                )
                .exceptionHandling(e -> e.authenticationEntryPoint(jwtAuthenticationEntryPoint))
                .addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of(
                "http://localhost:4000",
                "http://localhost:4200"
        ));
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(List.of("*"));
        configuration.setAllowCredentials(false);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
