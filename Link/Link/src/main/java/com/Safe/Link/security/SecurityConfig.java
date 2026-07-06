package com.Safe.Link.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
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
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtFilter jwtFilter;

    @Bean
    public UserDetailsService userDetailsService() {
        return username -> { throw new UsernameNotFoundException("Sin UserDetailsService"); };
    }


    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .csrf(csrf -> csrf.disable())
                .httpBasic(httpBasic -> httpBasic.disable())
                .formLogin(formLogin -> formLogin.disable())
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                        .requestMatchers(HttpMethod.POST,
                                "/api/usuarios/registro",
                                "/api/usuarios/registro/",
                                "/api/usuarios/login",
                                "/api/usuarios/login/",
                                "/api/asistente/preguntar",
                                "/api/asistente/preguntar/"
                        ).permitAll()
                        .requestMatchers(HttpMethod.GET,
                                "/api/educacion/consejos",
                                "/api/educacion/consejos/**",
                                "/api/soporte/faq",
                                "/api/soporte/faq/**",
                                "/api/puntos-seguros",
                                "/api/puntos-seguros/",
                                "/api/puntos-seguros/{id}",
                                "/api/puntos-seguros/tipo/{tipo}",
                                "/api/refugios/disponible",
                                "/api/items-kit/recomendados",
                                "/api/emergencia/numeros",
                                "/api/emergencia/numeros/**"
                        ).permitAll()
                        .requestMatchers(
                                "/swagger-ui/**",
                                "/swagger-ui.html",
                                "/v3/api-docs/**",
                                "/v3/api-docs"
                        ).permitAll()
                        .requestMatchers(HttpMethod.GET,
                                "/api/familias/usuario/**"
                        ).authenticated()
                        .requestMatchers(HttpMethod.POST,
                                "/api/familias/usuario/**"
                        ).authenticated()
                        .requestMatchers(HttpMethod.GET,
                                "/api/usuarios",
                                "/api/usuarios/",
                                "/api/usuarios/tipo/**",
                                "/api/familias",
                                "/api/familias/",
                                "/api/familias/**"
                        ).hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE,
                                "/api/usuarios/**",
                                "/api/familias/**"
                        ).hasRole("ADMIN")
                        .anyRequest().authenticated()
                )
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of(
                "https://safelink-front.onrender.com",
                "http://localhost:4200",
                "http://127.0.0.1:4200"
        ));
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(List.of("Authorization", "Content-Type", "Accept", "Accept-Language", "Origin"));
        configuration.setExposedHeaders(List.of("Authorization"));
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
