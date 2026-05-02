/*
 * This class defines Spring Security configuration for the application.
 * For now, all requests are allowed so APIs can be tested during development.
 * Later this configuration will be replaced with JWT-based security.
 */
package com.dreamcart.backend.security;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {
    /*
     * Defines the security filter chain.
     * CSRF is disabled for API testing, and all requests are temporarily permitted.
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth.anyRequest().permitAll())
                .httpBasic(Customizer.withDefaults());
        return http.build();
    }
}
