package br.com.projetos3.visita.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(auth -> auth
                        .anyRequest().permitAll() // libera tudo
                )
                .csrf(csrf -> csrf.disable()) // desativa CSRF (útil pra dev)
                .formLogin(login -> login.disable()) // remove tela de login
                .logout(logout -> logout.disable()); // remove logout
        return http.build();
    }
}
