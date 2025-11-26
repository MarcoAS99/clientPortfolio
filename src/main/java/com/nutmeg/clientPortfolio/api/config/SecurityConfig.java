package com.nutmeg.clientPortfolio.api.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // Disable CSRF if this is a stateless REST API
                .csrf(csrf -> csrf.disable())

                // Define URL authorization rules
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/swagger-ui/**", "/v3/api-docs/**").permitAll() // public access to Swagger
                        .requestMatchers("/api/client/**").permitAll()                    // public endpoints
                        .anyRequest().authenticated()                                     // everything else requires authentication
                )

                // Optional: HTTP Basic (for testing) or use JWT for REST
                .httpBasic();

        return http.build();
    }

}