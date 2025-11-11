package br.com.projetos3.visita.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/admin/**").authenticated() // 1. Protege as rotas de admin
                        .anyRequest().permitAll()                     // 2. Libera todo o resto
                )
                .formLogin(login -> login
                        .loginPage("/login") // 3. Define sua página de login customizada
                        .defaultSuccessUrl("/admin", true) // Para onde ir após o login
                        .permitAll()
                )
                .logout(logout -> logout.logoutSuccessUrl("/").permitAll())
                .csrf(csrf -> csrf.disable()); // CSRF desabilitado para simplificar
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // Bean para criar o usuário gestor
    @Bean
    public InMemoryUserDetailsManager userDetailsService(PasswordEncoder passwordEncoder) {
        UserDetails user = User.builder()
                .username("admin")
                .password(passwordEncoder.encode("password"))
                .roles("ADMIN")
                .build();
        return new InMemoryUserDetailsManager(user);
    }
}