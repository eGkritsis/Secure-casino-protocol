package com.aueb.casino.netsec.demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfiguration;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfiguration{

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable)
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/api/auth/register", "/api/auth/login",  "/css/**").permitAll() // Allow registration and login
                .requestMatchers("/game/play").authenticated() // Allow authenticated access to /game/play
                .requestMatchers("/api/auth/home").authenticated() // Home requires authentication
                .anyRequest().authenticated() // All other requests require authentication
                .and()
                .addFilterBefore(new JwtRequestFilter(), UsernamePasswordAuthenticationFilter.class)
            )
            .formLogin(form -> form
                .loginPage("/api/auth/login") // Custom login page
                .loginProcessingUrl("/api/auth/login") // URL to submit the login form
                .defaultSuccessUrl("/api/auth/home", true) // Redirect to home after successful login
                .permitAll()
            )
            .logout(logout -> logout
                .logoutUrl("/api/auth/logout") // Logout endpoint
                .logoutSuccessUrl("/api/auth/login") // Redirect to login page after logout
                .permitAll()
            );
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}