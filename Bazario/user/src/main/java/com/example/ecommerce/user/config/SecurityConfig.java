package com.example.ecommerce.user.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class SecurityConfig {

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  // Needed for login logic
  @Bean
  public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
    return config.getAuthenticationManager();
  }

  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    // http.csrf(csrf -> csrf.disable())
    //     .authorizeHttpRequests(requests -> requests
    //         .requestMatchers("/users/register", "/users/login").permitAll() // ✅ Public endpoints
    //         .anyRequest().authenticated())
    //     .formLogin(form -> form.disable()); // ❗ Disables Spring's built-in form login

    // return http.build();
     http.csrf(csrf -> csrf.disable()).authorizeHttpRequests(req -> req.anyRequest().permitAll());
        return http.build();
  }
}
