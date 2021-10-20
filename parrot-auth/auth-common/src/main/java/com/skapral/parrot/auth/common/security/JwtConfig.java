package com.skapral.parrot.auth.common.security;

import com.skapral.parrot.auth.common.jwt.JwtAuthEntryPoint;
import com.skapral.parrot.auth.common.jwt.JwtAuthFilter;
import com.skapral.parrot.auth.common.jwt.JwtUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource("classpath:security.properties")
public class JwtConfig {
    @Bean
    public JwtUtils jwtUtils() {
        return new JwtUtils();
    }

    @Bean
    public JwtAuthEntryPoint jwtAuthEntryPoint() {
        return new JwtAuthEntryPoint();
    }

    @Bean
    public JwtAuthFilter jwtAuthFilter(JwtUtils jwtUtils) {
        return new JwtAuthFilter(jwtUtils);
    }
}
