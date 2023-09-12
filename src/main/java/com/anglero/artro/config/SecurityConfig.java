package com.anglero.artro.config;

import com.anglero.artro.config.jwt.JwtAuthenticationFilter;
import com.anglero.artro.config.jwt.JwtAuthorizationFilter;
import com.anglero.artro.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.filter.CorsFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final CorsFilter corsFilter;
    private final UserRepository userRepository;

    @Autowired
    private CorsConfig corsConfig;

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer :: disable);
        http.sessionManagement(sessionManagement ->
                sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        );
        http.addFilter(corsFilter);
        http.formLogin(AbstractHttpConfigurer :: disable);
        http.httpBasic(AbstractHttpConfigurer :: disable);
        http.apply(new MyCustomDsl());
        http.authorizeHttpRequests(authrize -> authrize
            .requestMatchers("/api/v1/user/**").hasAnyRole("USER", "MANAGER", "ADMIN")
            .requestMatchers("/api/v1/manager/**").hasAnyRole("MANAGER", "ADMIN")
            .requestMatchers("/api/v1/admin/**").hasAnyRole("ADMIN")
            .anyRequest().permitAll()
        );
        return http.build();
    }

    public class MyCustomDsl extends AbstractHttpConfigurer<MyCustomDsl, HttpSecurity> {
        @Override
        public void configure(HttpSecurity http) throws Exception {
            AuthenticationManager authenticationManager = http.getSharedObject(AuthenticationManager.class);
            http
                .addFilter(corsConfig.corsFilter())
                .addFilter(new JwtAuthenticationFilter(authenticationManager))
                .addFilter(new JwtAuthorizationFilter(authenticationManager, userRepository));
        }
    }
}
